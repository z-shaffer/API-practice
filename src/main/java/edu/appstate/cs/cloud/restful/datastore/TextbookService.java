package edu.appstate.cs.cloud.restful.datastore;

import com.google.cloud.datastore.*;
import edu.appstate.cs.cloud.restful.models.Textbook;
import edu.appstate.cs.cloud.restful.pubsub.Publish;
import edu.appstate.cs.cloud.restful.pubsub.Topics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TextbookService {
    private Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final String ENTITY_KIND = "Textbook";
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind(ENTITY_KIND);

    public Key createTextbook(Textbook textbook) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity textbookEntity = Entity.newBuilder(key)
                .set(Textbook.TITLE, textbook.getTitle())
                .set(Textbook.AUTHORS, textbook.getAuthors())
                .set(Textbook.SUBJECT, textbook.getSubject())
                .set(Textbook.PUBLISHER, textbook.getPublisher())
                .set(Textbook.YEAR, textbook.getYear())
                .set(Textbook.IMAGE_LINK, textbook.getImageLink())
                .build();
        datastore.put(textbookEntity);

        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_CREATED)
                .sendId(key.getId())
                .build()
                .publish();

        return key;
    }

    public List<Textbook> getAllTextbooks() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildTextbooks(entities);
    }

    public List<Textbook> getAllTextbooksForSubject(String subject) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq(Textbook.SUBJECT, subject))
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildTextbooks(entities);
    }

    public Textbook getTextbook(long textbookId) {
       // TODO: Add code to get a subject by ID here
       Entity textbookEntity = datastore.get(keyFactory.newKey(textbookId));
       // TODO: Do we need to publish to a topic? If so, add the code here.

       // TODO: Remove this once you can return a real object
       return entityToTextbook(textbookEntity);
    }

    public void deleteTextbook(long textbookId) {
        // TODO: Add code to delete a subject by ID here
        datastore.delete(keyFactory.newKey(textbookId));
        // TODO: Do we need to publish to a topic? If so, add the code here.
        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_DELETED)
                .sendId(textbookId)
                .build()
                .publish();
    }

    public void updateTextbook(Textbook textbook) {
        // TODO: Add code to update a subject by ID here
        Entity textbookEntity = Entity
                .newBuilder(datastore.get(keyFactory.newKey(textbook.getId())))
                .set(Textbook.TITLE, textbook.getTitle())
                .set(Textbook.AUTHORS, textbook.getAuthors())
                .set(Textbook.SUBJECT, textbook.getSubject())
                .set(Textbook.PUBLISHER, textbook.getPublisher())
                .set(Textbook.YEAR, textbook.getYear())
                .set(Textbook.IMAGE_LINK, textbook.getImageLink())
                .build();
        datastore.update(textbookEntity);
        // TODO: Do we need to publish to a topic? If so, add the code here.
        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_UPDATED)
                .sendId(textbook.getId())
                .build()
                .publish();
    }

    private List<Textbook> buildTextbooks(Iterator<Entity> entities) {
        List<Textbook> textbooks = new ArrayList<>();
        entities.forEachRemaining(entity -> textbooks.add(entityToTextbook(entity)));
        return textbooks;
    }

    private Textbook entityToTextbook(Entity entity) {
        return new Textbook.Builder()
                .withId(entity.getKey().getId())
                .withTitle(entity.getString(Textbook.TITLE))
                .withAuthors(entity.getString(Textbook.AUTHORS))
                .withSubject(entity.getString(Textbook.SUBJECT))
                .withPublisher(entity.getString(Textbook.PUBLISHER))
                .withYear(entity.getLong(Textbook.YEAR))
                .withImageLink(entity.getString(Textbook.IMAGE_LINK))
                .build();
    }

}
