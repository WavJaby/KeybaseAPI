package com.wavjaby.bot;

import com.wavjaby.bot.queue.SendQueue;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class DataSender {
    private final String TAG = "[DataSender] ";
    private BufferedWriter writer;

    private Queue<SendQueue> requests = new LinkedList<>();

    private BufferedReader reader;
    private boolean start;
    DataSender(String... commands) {
        new Thread(() -> {
            try {
                start = true;
                Process proc = new ProcessBuilder(commands).start();
                InputStream in = proc.getInputStream();
                OutputStream out = proc.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(out));
                reader = new BufferedReader(new InputStreamReader(in));
                System.out.println(TAG + "Sender start");
                while (start) {
                    String newLine = reader.readLine();
                    SendQueue sendQueue = requests.poll();
                    if (sendQueue != null)
                        sendQueue.onResult(newLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(TAG + "Sender stop");
        }).start();
    }

    public void stop() {
        start = false;
    }

    public String send(String data) {
        CountDownLatch count = new CountDownLatch(1);
        StringBuilder builder = new StringBuilder();
        try {
            SendQueue queue = new SendQueue() {
                @Override
                public void onResult(String result) {
                    count.countDown();
                    builder.append(result);
                }
            };
            send(data, queue);
            count.await();
            return builder.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void send(String data, SendQueue queue) {
        try {
            requests.offer(queue);
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendCommandCLI(String... commands) {
        try {
            Process proc = new ProcessBuilder(commands).start();
            proc.waitFor();
            InputStream in = proc.getInputStream();
            return readResult(in);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readResult(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int length;
            byte[] buff = new byte[1024];
            while ((length = in.read(buff)) > 0) {
                out.write(buff, 0, length);
            }
            return out.toString("UTF8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
