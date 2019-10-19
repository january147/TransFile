package com.january.transfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DataTrans {
    public void test() throws IOException {
        Socket session_s = new Socket("127.0.0.1", 19999);
        String message = "hello this is a test";
        OutputStream sending_stream = session_s.getOutputStream();
        sending_stream.write(message.getBytes());
        session_s.close();
    }
}
