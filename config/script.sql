   ALTER TABLE users ADD INDEX idx_email (email);

    create table interview_answers (
        score integer,
        id bigint not null auto_increment,
        question_id bigint not null,
        ai_feedback TEXT,
        answer_text TEXT not null,
        audio_url varchar(255),
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table interview_answers (
        score integer,
        id bigint not null auto_increment,
        question_id bigint not null,
        ai_feedback TEXT,
        answer_text TEXT not null,
        audio_url varchar(255),
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.384+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    create table interview_feedback (
        total_score integer,
        id bigint not null auto_increment,
        session_id bigint not null,
        improvement_suggestions TEXT,
        strengths TEXT,
        weaknesses TEXT,
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table interview_feedback (
        total_score integer,
        id bigint not null auto_increment,
        session_id bigint not null,
        improvement_suggestions TEXT,
        strengths TEXT,
        weaknesses TEXT,
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.402+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    create table interview_questions (
        max_score integer,
        id bigint not null auto_increment,
        session_id bigint not null,
        difficulty varchar(255),
        question_text TEXT not null,
        topic varchar(255),
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table interview_questions (
        max_score integer,
        id bigint not null auto_increment,
        session_id bigint not null,
        difficulty varchar(255),
        question_text TEXT not null,
        topic varchar(255),
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.419+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    create table interview_responses (
        score integer,
        id bigint not null auto_increment,
        question_id bigint not null,
        ai_remarks TEXT,
        answer_text TEXT,
        audio_url varchar(255),
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table interview_responses (
        score integer,
        id bigint not null auto_increment,
        question_id bigint not null,
        ai_remarks TEXT,
        answer_text TEXT,
        audio_url varchar(255),
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.436+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    create table interview_sessions (
        ended_at datetime(6),
        id bigint not null auto_increment,
        started_at datetime(6),
        user_id bigint not null,
        difficulty varchar(255),
        domain varchar(255),
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table interview_sessions (
        ended_at datetime(6),
        id bigint not null auto_increment,
        started_at datetime(6),
        user_id bigint not null,
        difficulty varchar(255),
        domain varchar(255),
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.454+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    create table users (
        created_at datetime(6),
        id bigint not null auto_increment,
        email varchar(255) not null,
        full_name varchar(255) not null,
        password varchar(255),
        role varchar(255) not null,
        primary key (id)
    ) engine=InnoDB
Hibernate:
    create table users (
        created_at datetime(6),
        id bigint not null auto_increment,
        email varchar(255) not null,
        full_name varchar(255) not null,
        password varchar(255),
        role varchar(255) not null,
        primary key (id)
    ) engine=InnoDB
2026-02-09T00:36:32.471+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_feedback
       add constraint UKc2hutewv6l42o779pqxkeg1xo unique (session_id)
Hibernate:
    alter table interview_feedback
       add constraint UKc2hutewv6l42o779pqxkeg1xo unique (session_id)
2026-02-09T00:36:32.490+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table users
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email)
Hibernate:
    alter table users
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email)
2026-02-09T00:36:32.504+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_answers
       add constraint FK4kqx5jj1pfofmdtxoihtq2sg5
       foreign key (question_id)
       references interview_questions (id)
Hibernate:
    alter table interview_answers
       add constraint FK4kqx5jj1pfofmdtxoihtq2sg5
       foreign key (question_id)
       references interview_questions (id)
2026-02-09T00:36:32.563+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_feedback
       add constraint FK6l98i4gjyh1vbag00lkq9s7uj
       foreign key (session_id)
       references interview_sessions (id)
Hibernate:
    alter table interview_feedback
       add constraint FK6l98i4gjyh1vbag00lkq9s7uj
       foreign key (session_id)
       references interview_sessions (id)
2026-02-09T00:36:32.613+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_questions
       add constraint FKf1qet5d65jvfag4tqnc3oqyo8
       foreign key (session_id)
       references interview_sessions (id)
Hibernate:
    alter table interview_questions
       add constraint FKf1qet5d65jvfag4tqnc3oqyo8
       foreign key (session_id)
       references interview_sessions (id)
2026-02-09T00:36:32.663+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_responses
       add constraint FK5w9g4f9wabpxiku30wkhg3qyk
       foreign key (question_id)
       references interview_questions (id)
Hibernate:
    alter table interview_responses
       add constraint FK5w9g4f9wabpxiku30wkhg3qyk
       foreign key (question_id)
       references interview_questions (id)
2026-02-09T00:36:32.711+05:30 DEBUG 45732 --- [interviewchatbot] [           main] org.hibernate.SQL                        :
    alter table interview_sessions
       add constraint FKoa5rgsdu7rqa8y74yph1fuqe5
       foreign key (user_id)
       references users (id)
Hibernate:
    alter table interview_sessions
       add constraint FKoa5rgsdu7rqa8y74yph1fuqe5
       foreign key (user_id)
       references users (id)

