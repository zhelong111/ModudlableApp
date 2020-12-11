package network.retrofit.comm;

import java.io.IOException;
import network.retrofit.downloader.FileTransferListener;
import network.retrofit.downloader.RetroDownloadUtil;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/************************************************
 * Function：
 * Author: Bruce.Zhou
 * Date: 2020/12/11
 * Email: zhoul5@bngrp.com
 *************************************************/
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private FileTransferListener listener;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, FileTransferListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != listener) {
                    listener.onProgress(totalBytesRead, responseBody.contentLength());
                }
                return bytesRead;
            }
        };
    }
}
