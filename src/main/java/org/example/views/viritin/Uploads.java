package org.example.views.viritin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import org.example.Addon;
import org.example.DefaultLayout;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.upload.UploadFileHandler;
import org.vaadin.firitin.fields.CommaSeparatedStringField;
import org.vaadin.firitin.fields.DurationField;
import org.vaadin.firitin.fields.ElementCollectionField;
import org.vaadin.firitin.fields.EnumSelect;
import org.vaadin.firitin.form.BeanValidationForm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Route(layout = DefaultLayout.class)
@MenuItem(icon = VaadinIcon.FOLDER_ADD, parent = ViritinMenuGroup.class)
@Addon("flow-viritin")
public class Uploads extends VVerticalLayout {

    public Uploads() {
        add(new RichText().withMarkDown("""
        # UploadFileHandler - uploads with steroids
        
        The upload API in Vaadin core got a lot better in 24.8, but there are
        still couple of superpowers in Viritin (hopefully coming to core in the future):
        
         * Does not require odd configurations in Spring Boot for multipart upload size limits. Note that 
           your reverse proxy (e.g. nginx) might still limit the upload size, in this deployment 50m.
         * Uploads are truly streamed, you can handle files while they are being uploaded
         * Provides file metadata like file name, mime type when handling dropped files (in Flow 24.8.5 only when picked via dialog, regression)
         * Supports folder uplooads both via dialog and drag-and-drop
        
        Below a configuration that counts lines from folders that are picked with the dialog (or
          files in any folder structure dropped) in a files while they are being uploaded.
          So either pick a folder that contains some files or drop files/folders to the upload area.
        """));

        UploadFileHandler fileHandlerExample = new UploadFileHandler((content, metadata) -> {
            try {
                // This is true streaming, we are reading the file contents while it is being uploaded.
                // Also, no file size limits on Spring Boot, so you can upload large files as well and still
                // process them in a memory efficient way.
                int b;
                int count = 0;
                while ((b = content.read()) != -1) {
                    if (b == "\n".getBytes()[0]) {
                        count++;
                    }
                }
                int finalCount = count;
                // return value is a Runnable where you can do UI updates (no Push required)
                return () -> {
                    add(new Div("%s lines in %s (folderpath: %s)".formatted(finalCount, metadata.fileName(), metadata.folderPath())));
                };
            } catch (Exception e) {
                return () -> Notification.show("Error processing file: " + e.getMessage());
            }
        })
                .chooseFolders() // make the OS dialog select folders
                .withUploadButton(new VButton(VaadinIcon.FOLDER, "Choose folder..."));
        add(fileHandlerExample);

    }

}

