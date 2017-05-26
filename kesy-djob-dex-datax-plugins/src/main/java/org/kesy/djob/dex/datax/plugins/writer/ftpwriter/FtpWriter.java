/**
 * This file created at 2014-3-4.
 *

 */
package org.kesy.djob.dex.datax.plugins.writer.ftpwriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.kesy.djob.dex.datax.common.plugin.LineReceiver;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Writer;
import org.kesy.djob.dex.line.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link FtpWriter}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class FtpWriter extends Writer {
	private static Logger logger = LoggerFactory.getLogger(FtpWriter.class.getCanonicalName());
	
	private String host = "";
	private String port = "";
	private String username = "";
	private String password = "";
	private String path = "";
	private String filename = "";
	private String PREFIX = "";
	private String nullString = " ";
	private char FIELD_SPLIT = '|';
	private String ENCODING = "UTF-8";
	private boolean printable = true;
	private int writeBufferSize = 1024 * 1024;
	private int lineSize = 1;
	private FTPClient ftp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.common.plugin.Writer#commit()
	 */
	@Override
	public int commit() {
		// TODO implement Writer.commit
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.common.plugin.Writer#connect()
	 */
	@Override
	public int connect() {
		ftp = new FTPClient();
		ftp.setControlEncoding(ENCODING);
		ftp.enterLocalPassiveMode();
		int reply;
		try {
			ftp.connect(host, Integer.valueOf(port));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//
		logger.info(String.format(
				"FTP writer connect to:FTP://%s:%s%s successfully", host, port, path));
		try {
			ftp.login(username, password);
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		logger.info(String.format(
				"FTP writer login to:FTP://%s:%s%s  successfully,username:%s",
				host, port, path, username));
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
		try {
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (!createDirecroty(ftp, path)) {
				return PluginStatus.FAILURE.value();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			logger.info(String.format(
					"Now FTP Client WorkingDirectory is %s  .....", ftp
							.printWorkingDirectory()));
		} catch (IOException e) {
		}
		ftp.setBufferSize(5 * 1024);
		try {
			ftp.setSoTimeout(60 * 1000);
		} catch (SocketException e) {
		}
		ftp.setDataTimeout(60 * 1000);
		return PluginStatus.SUCCESS.value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.common.plugin.Writer#finish()
	 */
	@Override
	public int finish() {
		try {
			ftp.logout();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.common.plugin.Writer#init()
	 */
	@Override
	public int init() {
		// TODO implement Writer.init
		logger.info("FTP wiriter being init......");
		this.host = param.getValue(ParamKey.host, "");
		this.port = param.getValue(ParamKey.port, "21");
		this.username = param.getValue(ParamKey.username, "");
		this.password = param.getValue(ParamKey.password, "");
		this.path = param.getValue(ParamKey.path, "/").endsWith("/") ? param
				.getValue(ParamKey.path, "/") : (param.getValue(ParamKey.path,
				"/") + "/");
		this.filename = param.getValue(ParamKey.filename, "");
		this.ENCODING = param.getValue(ParamKey.encoding, "UTF-8");
		this.FIELD_SPLIT = param.getCharValue(ParamKey.fieldSplit, '|');
		logger.info("FTP wiriter  init finished......");
		return PluginStatus.SUCCESS.value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.common.plugin.Writer#startWrite(org.kesy.djob.dex.datax.common
	 * .plugin.LineReceiver)
	 */
	@Override
	public int startWrite(LineReceiver linereceiver) {
		// TODO implement Writer.startWrite
		Line line;
		logger.info(String.format("FTP writer begin write to:FTP://%s:%s%s/%s",
				host, port, path, filename));
		StringBuffer sBuffer = new StringBuffer();
		int i = 0;
		while ((line = linereceiver.getFromReader()) != null) {
			if (this.printable) {
				if (i <= lineSize) {
					i++;
				} else {
				    logger.info(String.format("====**********====="+ENCODING)); 
					try {
						ftp.appendFile(filename, new ByteArrayInputStream(
								sBuffer.toString().getBytes(ENCODING)));
						sBuffer.delete(0, sBuffer.length());
						i = 0;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sBuffer.append(makeVisual(line));

			} else {
				/* do nothing */
			}
		}
		if (sBuffer.length() > 0) {
			try {
				ftp.appendFile(filename, new ByteArrayInputStream(sBuffer
						.toString().getBytes()));
				sBuffer.delete(0, sBuffer.length());
				i = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return PluginStatus.SUCCESS.value();
	}

	public boolean createDirecroty(FTPClient ftp, String path)
			throws IOException {
		boolean success = true;
		String directory = path.endsWith("/") ? path : (path + "/");
		if (!directory.equalsIgnoreCase("/")
				&& !ftp.changeWorkingDirectory(directory)) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(path.substring(start, end));
				if (!ftp.changeWorkingDirectory(subDirectory)) {
					if (ftp.makeDirectory(subDirectory)) {
						if (!ftp.changeWorkingDirectory(subDirectory)) {
							logger
									.info(String
											.format(
													"changeWorkingDirectory to %s failed .....",
													ftp.printWorkingDirectory()
															+ "/"
															+ subDirectory));
							return false;

						}
						logger.info(String.format(
								"changeWorkingDirectory to %s sucessful .....",
								ftp.printWorkingDirectory()));
					} else {
						logger.info(String.format(
								"Create Directory Folder %s sucessful .....",
								ftp.printWorkingDirectory() + "/"
										+ subDirectory));
						success = false;
						return success;
					}
				}
				start = end + 1;
				end = directory.indexOf("/", start);
				if (end <= start) {
					break;
				}
			}
		}
		logger.info(String.format(
				"Now FTP Client WorkingDirectory is %s  .....", ftp
						.printWorkingDirectory()));
		return success;
	}

	private String makeVisual(Line line) {
		if (line == null || line.getFieldNum() == 0) {
			return this.PREFIX + "\n";
		}

		int i = 0;
		String item = null;
		int num = line.getFieldNum();
		StringBuilder sb = new StringBuilder();

		sb.append(this.PREFIX);
		for (i = 0; i < num; i++) {
			item = line.getField(i);
			if (null == item) {
				sb.append(nullString);
			} else {
				sb.append(item);
			}

			if (i != num - 1) {
				sb.append(FIELD_SPLIT);
			} else {
				sb.append('\n');
			}
		}
		if (sb.toString().getBytes().length < writeBufferSize) {
			lineSize = writeBufferSize / sb.toString().getBytes().length;
		}
		return sb.toString();
	}

}
