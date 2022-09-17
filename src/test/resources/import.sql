-- userId -> 4 -> max
-- userId -> 5 -> el
-- userId -> 9 -> lucas
insert into comments (user_id, content, date_posted) values (5, 'Very cute post', 1663241348579);
insert into comments (user_id, content, date_posted) values (5, 'I totally love this', 1663241370613);

insert into posts (user_id, date_posted, likes, dislikes, text) values (4, 1663239692000, 2, 0, 'It would be great if we could save every stray cat and dog on this planet. And also in the upside down');

insert into reactions (user_id, reaction_type, post_id) values (5, 0, 1);
insert into reactions (user_id, reaction_type, post_id) values (9, 0, 1);

insert into posts_comments (post_id, comments_id) values (1, 1);
insert into posts_comments (post_id, comments_id) values (1, 2);

insert into posts_reactions (post_id, reactions_id) values (1, 1);
insert into posts_reactions (post_id, reactions_id) values (1, 2);

