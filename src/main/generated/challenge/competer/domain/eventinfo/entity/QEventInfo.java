package challenge.competer.domain.eventinfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEventInfo is a Querydsl query type for EventInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventInfo extends EntityPathBase<EventInfo> {

    private static final long serialVersionUID = 2020717237L;

    public static final QEventInfo eventInfo = new QEventInfo("eventInfo");

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QEventInfo(String variable) {
        super(EventInfo.class, forVariable(variable));
    }

    public QEventInfo(Path<? extends EventInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEventInfo(PathMetadata metadata) {
        super(EventInfo.class, metadata);
    }

}

