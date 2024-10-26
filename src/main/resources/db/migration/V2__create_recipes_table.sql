create sequence recipes_id_seq start with 1 increment by 1;

create table recipes
(
    recipe_id bigint not null default nextval('recipes_id_seq'),
    title varchar(255) not null,
    image_url varchar(255) not null,
    description varchar(255) not null,
    is_vegetarian boolean not null,
    user_id bigint not null,
    created_at timestamp not null default now(),
    updated_at timestamp,
    primary key (recipe_id),
    constraint foreign_recipe_user_id foreign key (user_id) references users(user_id)
);

insert into recipes(title, description, is_vegetarian, image_url, user_id)
values ('Panna Cotta',
        'Panna cotta (Italian: cooked cream) is an Italian dessert of sweetened cream thickened with gelatin and molded.',
        false,
        'https://pescetarian.kitchen/wp-content/uploads/2017/01/IMG_1061.jpg',
        1),

       ('Crème caramel',
        'Caramel candy, or "caramels", and sometimes called "toffee" (though this also refers to other types of candy), is a soft, dense, chewy candy made by boiling a mixture of milk or cream, sugar(s), glucose, butter, and vanilla (or vanilla flavoring).',
        false,
        'https://recipe30.com/wp-content/uploads/2016/10/creme-caramel-2-848x477.jpg',
        2)