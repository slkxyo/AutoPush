package org.slkxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.io.PrintWriter;

@Slf4j
@AllArgsConstructor
public class Writer {
    private AttributedStyle style;

    static class Builder{
        private AttributedStyle style = AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE);

        public  Writer build(){
            return new Writer(this.style);
        }

        public Builder green(){
            this.style = AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
            return this;
        }


        public Builder red(){
            this.style = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
            return this;
        }

        public Builder yellow(){
            this.style = AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW);
            return this;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public void write(String text){
        try(
                Terminal terminal = TerminalBuilder.builder().system(true).build()
        ) {
            log.info(text);
            AttributedString str = new AttributedString(text, this.style);
            terminal.writer().println(str.toAnsi());
            terminal.flush();
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
