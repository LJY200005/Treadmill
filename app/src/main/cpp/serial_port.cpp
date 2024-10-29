// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("myapplication");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("myapplication")
//      }
//    }

#include <jni.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <string>
#include <iostream>
#include <android/log.h>
#include <errno.h>


#define LOG_TAG "SerialPortNative"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


int setSerialAtributes(int fd, int speed){
    struct termios tty;

    if(tcgetattr(fd, &tty) != 0){
        perror("tcgetattr");
        return -1;
    }

    cfsetospeed(&tty, speed);
    cfsetispeed(&tty, speed);

    tty.c_cflag |= (CLOCAL | CREAD);    //使能接受，设置本地模式
    tty.c_cflag &= ~CSIZE;
    tty.c_cflag |= CS8;              // 8-bit characters

    tty.c_cflag &= ~(PARENB | PARODD); // No parity
    tty.c_cflag &= ~CSTOPB;            // 1 stop bit

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("tcsetattr");
        return -1;
    }
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Serialport_openPort(JNIEnv *env, jobject obj, jstring portName, jint baudRate) {
    const char *port = env->GetStringUTFChars(portName, nullptr);
    int fd = open(port, O_RDWR | O_NOCTTY | O_SYNC);
    env->ReleaseStringUTFChars(portName, port);

    if (fd == -1) {
        perror("open");
        LOGE("Failed to open port: %s (errno: %d)", strerror(errno), errno);
        return -1;
    }

    setSerialAtributes(fd, baudRate);
    return fd;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Serialport_closePort(JNIEnv *env, jobject obj, jint fd) {
    return close(fd);
}