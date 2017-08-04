package actors;

import actor.messages.Messages;
import akka.actor.*;
import akka.japi.Procedure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * this actor reads the file line by line and sends them to
 * {@code WordsInLineActor} to count the words in line. Upon geting the results,
 * It sends the result to it's parent actor {@code WordCount}
 *
 * @author
 */
public class WordCountInAFileActor extends UntypedActor {

    private Long count = new Long(0);
    public static final String SHUTDOWN = "shutdown";
    Procedure<Object> shuttingDown = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            if (message.equals("job")) {
                getSender().tell("service unavailable, shutting down", getSelf());
            } else if (message instanceof Terminated) {
                getContext().stop(getSelf());
            }
        }
    };

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof File) {
            File file = (File) msg;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                getContext().actorOf(Props.create(WordsInLineActor.class)).tell(line, getSelf());
                //System.out.println(line);
            }
            br.close();
        } else if (msg instanceof Messages.FinalResultResponse) {

            Messages.FinalResultResponse resultResponse = (Messages.FinalResultResponse) msg;
            long lineCount = resultResponse.getCount();
            count+= lineCount;
            final Messages.FileResultResponse fileResultResponse=new Messages.FileResultResponse(count);
            getContext().parent().tell(fileResultResponse, getSelf());
            System.out.println("WordCountInAFileActor: "+fileResultResponse.getCount());
        }
        else if (msg.equals(SHUTDOWN)) {
            System.out.println("In shutdown");
            Iterator<ActorRef> iterator=this.getContext().getChildren().iterator();
            while (iterator.hasNext())
            {
                getContext().getChild(iterator.next().toString()).tell(PoisonPill.getInstance(), getSelf());
                getContext().become(shuttingDown);
            }
        }
        else {
            unhandled(msg);
        }
    }
}
