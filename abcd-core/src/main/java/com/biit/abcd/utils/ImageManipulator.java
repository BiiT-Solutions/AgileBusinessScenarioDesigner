package com.biit.abcd.utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class ImageManipulator {

	public static byte[] svgToPng(byte[] streamBytes) throws TranscoderException, IOException {
		PNGTranscoder t = new PNGTranscoder();
		TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(streamBytes));
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(ostream);

		t.transcode(input, output);

		ostream.flush();
		// ostream.close();
		return ostream.toByteArray();
	}

	public static byte[] svgToPng(byte[] streamBytes, float width, float height) throws TranscoderException, IOException {
		PNGTranscoder t = new PNGTranscoder();
		TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(streamBytes));
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(ostream);

		t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);
		t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
		t.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);

		t.transcode(input, output);

		ostream.flush();
		// ostream.close();
		return ostream.toByteArray();
	}
}
