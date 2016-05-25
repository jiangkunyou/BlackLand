package com.dlut.jky.app1.utils;

/**
 * Created by zhonghua on 2015/4/26.
 */
//------------------�ο�����---------------------------------
//
//1�����ֽڶ�ȡ�ļ�����
//2�����ַ���ȡ�ļ�����
//3�����ж�ȡ�ļ�����
//4�������ȡ�ļ�����

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadFromFile {
    /**
     * ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ���
     * @param fileName: �ļ�����
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�");
            // һ�ζ�һ���ֽ�
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�");
            // һ�ζ�����ֽ�
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            ReadFromFile.showAvailableBytes(in);
            // �������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ�
     * @param fileName: �ļ���
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�");
            // һ�ζ�һ���ַ�
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // ����windows�£�rn�������ַ���һ��ʱ����ʾһ�����С�
                // ������������ַ��ֿ���ʾʱ���ỻ�����С�
                // ��ˣ����ε�r����������n�����򣬽������ܶ���С�
                if (((char) tempchar) != 'r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("���ַ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�����ֽڣ�");
            // һ�ζ�����ַ�
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // �������ַ����ַ������У�charreadΪһ�ζ�ȡ�ַ���
            while ((charread = reader.read(tempchars)) != -1) {
                // ͬ�����ε�r����ʾ
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != 'r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == 'r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     * @param fileName: �ļ���
     */
    public static ArrayList<String> readFileByLines(String fileName) {
        ArrayList<String> arrs=new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
                System.out.println("line " + line + ": " + tempString);
                arrs.add("[line " + line + "]: " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return arrs;
    }

    /**
     * �����ȡ�ļ�����
     * @param fileName: �ļ���
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("�����ȡһ���ļ����ݣ�");
            // ��һ����������ļ�������ֻ����ʽ
            randomFile = new RandomAccessFile(fileName, "r");
            // �ļ����ȣ��ֽ���
            long fileLength = randomFile.length();
            // ���ļ�����ʼλ��
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // һ�ζ�10���ֽڣ�����ļ����ݲ���10���ֽڣ����ʣ�µ��ֽڡ�
            // ��һ�ζ�ȡ���ֽ�������byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    /**
     * ��ȡ�ļ�ָ��������
     * @param fileName: �ļ���
     */
    public static String readFileByLineNumber(String fileName, int lineNumber) {
        FileReader fr = null;
        BufferedReader br = null;
        String value = null;
        int i = 1;
        try {
            fr = new FileReader(fileName);
            br = new LineNumberReader(fr);
            while(i++ != lineNumber){
                br.readLine();
            }
            value = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    // �ļ����ݵ���������
    public static int getTotalLines(LineNumberReader lr) throws IOException {
        String s = lr.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = lr.readLine();
        }
        return lines;
    }

    /**
     * ��ʾ�������л�ʣ���ֽ���
     *
     * @param in
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("��ǰ�ֽ��������е��ֽ���Ϊ:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����LDA��dictionary.file-0�ļ�(LDA��docIndex����Ҳ����)
     * ����Ϊ��λ��ȡ�ֵ��ļ�,��ӳ���һ��map����
     * @param fileName: �ļ���
     * @return
     */
    public static Map<String, String> readDictionary(String fileName) {
        File file = new File(fileName);
        BufferedReader br = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            br = new BufferedReader(new FileReader(file));
            String temp = null;
            String[] temps;
            br.readLine();
            br.readLine();
            while((temp = br.readLine()) != null) {
                temps = temp.split(": ");
                if(temps.length == 4){
                    map.put(temps[3].substring(temps[3].indexOf('/') + 1).trim(), temps[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return map;
    }
}
