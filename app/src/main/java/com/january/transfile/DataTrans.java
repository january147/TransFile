package com.january.transfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class DataTrans {
    Socket session_s;

    public void connect(String ip, int port) throws IOException {
        session_s = new Socket(ip, port);
    }

    public boolean isConected() {
        boolean connected = (session_s == null)? false:session_s.isConnected();
        return connected;
    }

    public void send(byte[] data) throws IOException {
        OutputStream sending_stream = session_s.getOutputStream();
        sending_stream.write(data);
    }

    public void disconnect() {
        try {
            session_s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(session_s != null) {
            session_s.close();
        }
    }
}
