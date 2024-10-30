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
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

int setSerialAtributes(int fd, int buardRate){
    struct termios tty;
    speed_t speed;
    switch (buardRate) {
        case 9600: speed = B9600;break;
        case 19200: speed = B19200;break;
        case 57600: speed = B57600;break;
        case 115200: speed = B115200;break;
    }
    if(tcgetattr(fd, &tty) != 0){
        perror("tcgetattr");
        return -1;
    }

    cfsetospeed(&tty, speed);
    cfsetispeed(&tty, speed);
    //配置为原始模式
    cfmakeraw(&tty);
    //禁用软件流控制
    tty.c_cflag &= ~(IXON | IXOFF | IXANY);

    tty.c_cflag |= (CLOCAL | CREAD);    //使能接受，设置本地模式
    tty.c_cflag &= ~CRTSCTS;
    tty.c_cflag &= ~CSIZE;
    tty.c_cflag |= CS8;              // 8-bit characters

    tty.c_cflag &= ~(PARENB | PARODD); // No parity
    tty.c_cflag &= ~CSTOPB;            // 1 stop bit
    tty.c_iflag &= ~(BRKINT | ICRNL | INPCK | ISTRIP | IXON);

    if (tcsetattr(fd, TCSANOW, &tty) != 0) {
        perror("tcsetattr");
        return -1;
    }
    return 0;
}

int sendData(int fd, const char* data, size_t dataSize) {
    int bytesWritten = write(fd, data, dataSize);
    if (bytesWritten == -1) {
        LOGE("Failed to write data: %s (errno: %d)", strerror(errno), errno);
    }
    return bytesWritten;
}

int receiveData(int fd, char* buffer, size_t bufferSize) {  //这里需要知道字符长度，但是可能是变长的，还需要考虑设计
    int bytesRead = read(fd, buffer, bufferSize);
    if (bytesRead == -1) {
        LOGE("Failed to read data: %s (errno: %d)", strerror(errno), errno);
    }
    else
//        LOGD("Read data: %s (errno: %d)", strerror(errno), errno);
    return bytesRead;
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
Java_com_example_myapplication_Serialport_sendData(JNIEnv *env, jobject obj, jint fd, jbyteArray data) {
    jbyte *dataBytes = env->GetByteArrayElements(data, nullptr);
    jsize dataSize = env->GetArrayLength(data);
    int result = sendData(fd, (const char *) dataBytes, dataSize);
    env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Serialport_receiveData(JNIEnv *env, jobject obj, jint fd, jbyteArray buffer) {
    jbyte *bufferBytes = env->GetByteArrayElements(buffer, nullptr);
    jsize bufferSize = env->GetArrayLength(buffer);
    int bytesRead = receiveData(fd, (char *) bufferBytes, bufferSize);
    env->ReleaseByteArrayElements(buffer, bufferBytes, 0);
    return bytesRead;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Serialport_closePort(JNIEnv *env, jobject obj, jint fd) {
    return close(fd);
}
