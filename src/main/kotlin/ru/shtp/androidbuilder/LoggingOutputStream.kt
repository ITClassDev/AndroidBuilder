package ru.shtp.androidbuilder

import org.apache.log4j.Level
import org.apache.log4j.Logger
import java.io.IOException
import java.io.OutputStream

class LoggingOutputStream(private val logger: Logger, private val logLevel: Level = Level.INFO) : OutputStream() {
    private val buffer = StringBuilder()

    @Throws(IOException::class)
    override fun write(b: Int) {
        val c = b.toChar()
        if (c == '\n')
            flushBuffer()
        else
            buffer.append(c)
    }

    @Throws(IOException::class)
    override fun flush() {
        flushBuffer()
    }

    @Throws(IOException::class)
    override fun close() {
        flushBuffer()
    }

    private fun flushBuffer() {
        if (buffer.isEmpty())
            return
        logger.log(logLevel, buffer.toString())
        buffer.setLength(0)
    }
}