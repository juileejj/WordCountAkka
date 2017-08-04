package actors;

import actor.messages.Messages;
import akka.actor.UntypedActor;

/**
 * This actor counts number words in a single line
 *
 * @author
 */
public class WordsInLineActor extends UntypedActor {
    private Long count = new Long(0);
    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String line = (String)msg;
            String[] lineArray = line.split(" ");
            count+=lineArray.length;
            Messages.FinalResultResponse response = new Messages.FinalResultResponse(count);
            getSender().tell(response, getSelf());
            //System.out.println("WordsInLineActor: "+response.getCount());
        } else {
            unhandled(msg);
        }
    }
}
