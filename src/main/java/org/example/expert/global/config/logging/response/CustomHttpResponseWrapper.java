package org.example.expert.global.config.logging.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomHttpResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(outputStream);

    public CustomHttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public byte[] getResponseData() {
        writer.flush();
        return outputStream.toByteArray();
    }
}
