#ifndef USERMANAGEMENTSYSTEM_H
#define USERMANAGEMENTSYSTEM_H
#define MAXIMUM_GROUPS 4
#include <stdio.h>



typedef enum{
    GROUP_UNASSIGNED = -1,
    ROOT_USER = 0,
    SYSTEM_USERS = 201,
    OPERATOR_USERS = 1001,
    OBSERVER_USERS = 2001
} Groups;




typedef struct User {
    char Name[60];
    int age;
    char id[25];
    Groups groups[MAXIMUM_GROUPS];
    struct User* nextUserPointer;
} User;



User* init();
void free_users(User* root);
int count_users(User* root);
void print_user(User* root, char* id);
void print_system(User* root);
void create_user(User* root, char* name, int age, char* id, Groups groups[]);
void modify_user(User* root, char* id, char* new_name, int new_age, Groups new_groups[]);
void delete_user(User* root, char* id);
void add_group(User* root, char* id, Groups group);
void export_system(User* root);



#endif