package org.example.otherexamples;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AttachmentType;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import org.example.ExamplesLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Route(layout = ExamplesLayout.class)
public class NewDownloadAPI extends VerticalLayout {
    public NewDownloadAPI() {
        add("and why you should alwas use the simple lambda expression instead of helpers (see source from top right corner)...");

        add(new H2("Docs section: Download Content from InputStream"));

        Anchor downloadAttachmentByDocs = new Anchor(
                DownloadHandler.fromInputStream((event) -> {
                    try {
                        Attachment attachment = new AttachmentRepository().findById(0);
                        return new DownloadResponse(attachment.getData().getBinaryStream(),
                                attachment.getName(), attachment.getMime(), attachment.getSize());
                    } catch (Exception e) {
                        return DownloadResponse.error(500);
                    }
                }), "Download attachment docs example");
        // "New" Concepts in the above: DownloadHandler, static helper DownloadHandler.fromInputStream, DownloadEvent, InputStreamDownloadCallback, DownloadResponse
        add(downloadAttachmentByDocs);

        /*
         * This is my suggestion we should urge to all our users, because it is
         * simpler to understand and write.
         */
        Anchor downloadAttachmentSuggested = new Anchor(event -> {
            //This is the same as above, but uses "low level API" and JDK
            Attachment attachment = new AttachmentRepository().findById(0);
            event.setFileName(attachment.getName());
            event.setContentType(attachment.getMime());
            event.setContentLength(attachment.getSize());
            try {
                attachment.getData().getBinaryStream().transferTo(event.getOutputStream());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "Download attachment suggested");
        // "New" Concepts in the above: DownloadHandler, DownloadEvent
        add(downloadAttachmentSuggested);

        add(new H2("Same applies for class resources..."));

        Image logoImage = new Image(DownloadHandler.forClassResource(
                NewDownloadAPI.class, "/caf.png"), "Logo");
        add(logoImage);

        Image logoImageWithoutHelper = new Image(download -> {
            DownloadHandler.class.getResourceAsStream("/caf.png").transferTo(download.getOutputStream());
        }, "Logo");
        add(logoImageWithoutHelper);

        // Note instead of either above, would be better guide people letting SB or default
        // servlet serve the static stuff (provides almost for sure more optimal defaults e.g. for caching and for Range requests)
        // Copy of the image stored in src/main/resources/public/caf2.png
        Image logoImageSuggested = new Image("/caf2.png", "Logo");
        add(logoImageSuggested);


        add(new H2("forFile"));

        Anchor downloadDocs = new Anchor(DownloadHandler.forFile(new File("/etc/hosts")).inline(), AttachmentType.INLINE, "/etc/hosts inline");
        add(downloadDocs);

        Anchor downloadWithoutHelper = new Anchor(downdload -> {
            // Who on earth really uses old school File these days 🤷‍♂️
            Files.copy(Path.of("/etc/hosts"), downdload.getOutputStream());
        }, AttachmentType.INLINE, "/etc/hosts inline without helper");
        downloadWithoutHelper.setDownload(false);


        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory(
                (metadata, data) -> {
                    // Get other information about the file.
                    String fileName = metadata.fileName();
                    String mimeType = metadata.contentType();
                    long contentLength = metadata.contentLength();

                    // Do something with the file data...
                    // processFile(data, fileName);
                });


        Upload upload = new Upload(uploadEvent -> {
            // these three lines to make this similar to InMemoryUploadHandler
            String fileName = uploadEvent.getFileName();
            String contentType = uploadEvent.getContentType();
            long fileSize = uploadEvent.getFileSize();

            // Now this is all I see InMemoryUploadHandler doing for me
            byte[] data =  uploadEvent.getInputStream().readAllBytes();

            // Do something with the file data...
            // processFile(data, fileName);

        });



    }

    @FunctionalInterface
    public interface ImageHandler {
        /**
         * Gets a download handler for the avatar image for the given user.
         *
         * @param user
         *            the user for which to get a download handler with the
         *            image, not <code>null</code>
         * @return the download handler to use for the image, or
         *         <code>null</code> to not show use any avatar image for the
         *         given user
         */
        DownloadHandler getDownloadHandler(UserInfo user);
    }

    public static interface MyGroup {

        void setImageProvider(ImageHandler handler);

    }

    // Dummy implementation for the example classes used in the Directory, not relevant for the example below this

    public static class Attachment {
        private Long id;

        private Blob data;

        private Integer size;

        private String name = "image.png";

        private String mime = "image/png";

        public Blob getData() { return data; }
        public Integer getSize() { return size; }
        public String getName() { return name; }
        public String getMime() { return mime; }

        // other class fields and methods are omitted
    }

    public class AttachmentRepository {
        Attachment findById(long id) {
            Attachment a = new Attachment();
            try {
                a.size = NewDownloadAPI.class.getResourceAsStream("/caf.png").readAllBytes().length;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            a.data = new Blob() {
                @Override
                public long length() throws SQLException {
                    return 0;
                }

                @Override
                public byte[] getBytes(long pos, int length) throws SQLException {
                    return new byte[0];
                }

                @Override
                public InputStream getBinaryStream() throws SQLException {
                    return pngResource();
                }

                @Override
                public long position(byte[] pattern, long start) throws SQLException {
                    return 0;
                }

                @Override
                public long position(Blob pattern, long start) throws SQLException {
                    return 0;
                }

                @Override
                public int setBytes(long pos, byte[] bytes) throws SQLException {
                    return 0;
                }

                @Override
                public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
                    return 0;
                }

                @Override
                public OutputStream setBinaryStream(long pos) throws SQLException {
                    return null;
                }

                @Override
                public void truncate(long len) throws SQLException {

                }

                @Override
                public void free() throws SQLException {

                }

                @Override
                public InputStream getBinaryStream(long pos, long length) throws SQLException {
                    return null;
                }
            };
            return a;
        };
    }



    public static InputStream pngResource() {
        return NewDownloadAPI.class.getResourceAsStream("/caf.png");
    }

}
