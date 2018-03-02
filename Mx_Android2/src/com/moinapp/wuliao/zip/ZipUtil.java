package com.moinapp.wuliao.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
	private ZipFile zipFile;
	private ZipOutputStream zipOut;
	private int bufSize; // size of bytes
	private byte[] buf;
	private int readedBytes;

	public ZipUtil() {
		this(512);
	}

	public ZipUtil(int bufSize) {
		this.bufSize = bufSize;
		this.buf = new byte[this.bufSize];
	}

	public void doZip(String srcFile, String destFile) {
		File zipDir;
		String dirName;

		zipDir = new File(srcFile);
		dirName = zipDir.getName();
		try {
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));
			zipOut.setComment("comment");
			zipOut.setEncoding("GBK");
			zipOut.setMethod(ZipOutputStream.DEFLATED);

			zipOut.setLevel(Deflater.BEST_COMPRESSION);

			handleDir(zipDir, this.zipOut, dirName);
			this.zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param dir
	 * @param zipOut
	 * @param dirName
	 * @throws IOException
	 */
	private void handleDir(File dir, ZipOutputStream zipOut, String dirName) throws IOException {
		FileInputStream fileIn;
		File[] files;

		files = dir.listFiles();

		if (files.length == 0) {
			this.zipOut.putNextEntry(new ZipEntry(dirName));
			this.zipOut.closeEntry();
		} else {
			for (File fileName : files) {

				if (fileName.isDirectory()) {
					handleDir(fileName, this.zipOut, dirName + File.separator + fileName.getName() + File.separator);
				} else {
					fileIn = new FileInputStream(fileName);
					this.zipOut.putNextEntry(new ZipEntry(dirName + File.separator + fileName.getName()));

					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
						this.zipOut.write(this.buf, 0, this.readedBytes);
					}

					this.zipOut.closeEntry();
				}
			}
		}
	}

	public void unZip(String unZipfile, String destFile) {
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;

		try {
			this.zipFile = new ZipFile(unZipfile);

			for (Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(destFile + File.separator + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(file);
					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, this.readedBytes);
					}
					fileOut.close();

					inputStream.close();
				}
			}
			this.zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
}
