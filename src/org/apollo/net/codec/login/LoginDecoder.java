package org.apollo.net.codec.login;

import java.math.BigInteger;
import java.security.SecureRandom;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.security.IsaacRandomPair;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.ChannelBufferUtil;
import org.apollo.util.NameUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 * @author Graham
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The secure random number generator.
	 */
	private static final SecureRandom random = new SecureRandom();

	/**
	 * The username hash.
	 */
	private int usernameHash;

	/**
	 * The server-side session key.
	 */
	private long serverSeed;

	/**
	 * The reconnecting flag.
	 */
	private boolean reconnecting;

	/**
	 * The login packet length.
	 */
	private int loginLength;
	
	/**
	 * The RSA modulus key, 512 bits.
	 */
	private static final BigInteger RSA_MODULUS = new BigInteger("105343681541575057431393277612489186679347249197718172613508033563367447822360617114320874843264183218953559203543081875007930758989986613624908620003572141780732329417483269200656392922040616827361098832147776160551491701290021778374232945750265619508433372472340173685921672882839022904714676381139819257407");

	/**
	 * The RSA exponent key (or private), 512 bits.
	 */
	private static final BigInteger RSA_EXPONENT = new BigInteger("35605351020453012758636991201828707053026853792188462113948097280347778151473366643259863873127316210431363808500279332488528224398236011389068020228254666028991559256517425404937983261866368856098685614113737438147052752897641229121541025677835154506067788343533095314625075009215868203856666680176693135113");

	/**
	 * Creates the login decoder with the default initial state.
	 */
	public LoginDecoder() {
		super(LoginDecoderState.LOGIN_HANDSHAKE, true);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, LoginDecoderState state) throws Exception {
		switch (state) {
		case LOGIN_HANDSHAKE:
			return decodeHandshake(ctx, channel, buffer);
		case LOGIN_HEADER:
			return decodeHeader(ctx, channel, buffer);
		case LOGIN_PAYLOAD:
			return decodePayload(ctx, channel, buffer);
		default:
			throw new Exception("Invalid login decoder state");
		}
	}

	/**
	 * Decodes in the handshake state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeHandshake(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			usernameHash = buffer.readUnsignedByte();
			serverSeed = random.nextLong();
			ChannelBuffer resp = ChannelBuffers.buffer(9);
			resp.writeByte(LoginConstants.STATUS_EXCHANGE_DATA);
			resp.writeLong(serverSeed);
			channel.write(resp);
			setState(LoginDecoderState.LOGIN_HEADER);
		}
		return null;
	}

	/**
	 * Decodes in the header state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeHeader(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= 3) {
			int loginType = buffer.readUnsignedByte();

			if (loginType != LoginConstants.TYPE_STANDARD && loginType != LoginConstants.TYPE_RECONNECTION) {
				throw new Exception("Invalid login type");
			}

			reconnecting = loginType == LoginConstants.TYPE_RECONNECTION;

			loginLength = buffer.readUnsignedByte();
			setState(LoginDecoderState.LOGIN_PAYLOAD);
		}
		return null;
	}

	/**
	 * Decodes in the payload state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodePayload(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= loginLength) {
			ChannelBuffer payload = buffer.readBytes(loginLength);
			int releaseNumber = payload.readInt();
			int lowMemoryFlag = payload.readUnsignedByte();
			if (lowMemoryFlag != 0 && lowMemoryFlag != 1) 
				throw new Exception("Invalid value for low memory flag");

			boolean lowMemory = lowMemoryFlag == 1;
			
			for (int i = 0; i < 24; i++)
				payload.readByte();
			
			int[] archiveCrcs = new int[16];
			for (int i = 0; i < 16; i++) 
				archiveCrcs[i] = payload.readInt();

			loginLength--;
			
			int securePayloadLength = payload.readUnsignedByte();
			int secureBytesLength = (loginLength - 93);
			if (securePayloadLength != secureBytesLength) 
				throw new Exception("Secure payload length mismatch");
			
			byte[] secureBytes = new byte[secureBytesLength];
			payload.readBytes(secureBytes);
			ChannelBuffer securePayload = ChannelBuffers.wrappedBuffer(new BigInteger(secureBytes).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray());

			int secureId = securePayload.readUnsignedByte();
			if (secureId != 10) 
				throw new Exception("Invalid secure payload id");

			long clientSeed = securePayload.readLong();
			long reportedServerSeed = securePayload.readLong();
			
			if (reportedServerSeed != serverSeed) 
				throw new Exception("Server seed mismatch");
			
			String username = NameUtil.decodeBase37(securePayload.readLong());
			String password = ChannelBufferUtil.readString(securePayload);
			
			if (username.length() > 12 || password.length() > 20) 
				throw new Exception("Username or password too long");

			int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;

			IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int i = 0; i < seed.length; i++) 
				seed[i] += 50;
			IsaacRandom encodingRandom = new IsaacRandom(seed);

			PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash);
			IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);
			LoginRequest req = new LoginRequest(credentials, randomPair, reconnecting, lowMemory, releaseNumber, archiveCrcs);
			if (buffer.readable()) {
				return new Object[] { req, buffer.readBytes(buffer.readableBytes()) };
			} else {
				return req;
			}
		}
		return null;
	}

}
