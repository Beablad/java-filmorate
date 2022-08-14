merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 1, 'G' );
merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 2, 'PG' );
merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 3, 'PG-13' );
merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 4, 'R' );
merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 5 , 'NC-17');
merge into MPA_RATING (MPA_RATING_ID, MPA_RATING_NAME)
values ( 6 , 'Не указан');

merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 1, 'Комедия' );
merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 2, 'Драма' );
merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 3, 'Мультфильм' );
merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 4, 'Триллер' );
merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 5, 'Документальный' );
merge into GENRE (GENRE_ID, GENRE_NAME)
values ( 6, 'Боевик' );

/*merge into USERS (USER_ID, NAME, LOGIN, EMAIL, BIRTHDAY)
values ( 2, 'friend adipisicing', 'friend', 'friend@mail.ru', '1976-08-20');

merge into USERS (USER_ID, NAME, LOGIN, EMAIL, BIRTHDAY)
    values ( 1, 'Nick Name', 'dolore', 'mail@mail.ru', '1946-08-20');*/
/*
insert into USERS (USER_ID, NAME, LOGIN, EMAIL, BIRTHDAY)
    values ( 1, 'Nick Name', 'dolore', 'mail@mail.ru', '1946-08-20');

insert into USERS (USER_ID, NAME, LOGIN, EMAIL, BIRTHDAY)
    values ( 2, 'friend adipisicing', 'friend', 'friend@mail.ru', '1976-08-20');
*/
/*update USERS SET NAME = 'Nick Name',
                 LOGIN = 'dolore',
                 EMAIL = 'mail@mail.ru',
                 BIRTHDAY = '1946-08-20'
where USER_ID = 1;

update USERS SET NAME = 'friend adipisicing',
                 LOGIN = 'friend',
                 EMAIL = 'friend@mail.ru',
                 BIRTHDAY = '1976-08-20'
where USER_ID = 2;*/

