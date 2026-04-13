#include <stdio.h>
#include "UserManagementSystem.h"
#include <stdlib.h>
#include <string.h>

User* init(){

    User* root_user = (User*)malloc(sizeof(User));

    strcpy(root_user->Name, "root");

    root_user->age = 33;

    strcpy(root_user->id, "0");

    root_user->groups[0] = ROOT_USER;
    root_user->groups[1] = SYSTEM_USERS;
    root_user->groups[2] = OPERATOR_USERS;
    root_user->groups[3] = OBSERVER_USERS;
    root_user->nextUserPointer = NULL;

    return root_user;
}



void delete_user(User* root, char* id){
    if(root->groups[0] != ROOT_USER){
        printf("not root user");
        exit(0);
    }
    User* current = root;
    User* previous = NULL;
    while(current){
        if (strcmp(current->id, id) == 0){
            if(previous){
                previous->nextUserPointer = NULL;
            }
            free(current);
            return;
        }
        previous = current;
        current = current->nextUserPointer;
    }
}


char* get_group_name(Groups group) {
    if (group == ROOT_USER) {
        return "ROOT_USER";
    } else if (group == SYSTEM_USERS) {
        return "SYSTEM_USERS";
    } else if (group == OPERATOR_USERS) {
        return "OPERATOR_USERS";
    } else if (group == OBSERVER_USERS) {
        return "OBSERVER_USERS";
    } else {
        return "GROUP_UNASSIGNED";
    }
}


int count_users(User* root){

    if(root->groups[0] != ROOT_USER){
        printf("Not root user, cant calculate the users");
        exit(0);
    }
    int cnt = 0;

    User* current = root;

    while(current){
        ++cnt;

        current=current->nextUserPointer;

    }

    return cnt;

}





void free_users(User* root) {

    User* current = root;
    User* next;

    while (current) {

        next = current->nextUserPointer;
        free(current);
        current = next;

    }
}


void modify_user(User* root, char* id, char* new_name, int new_age, Groups new_groups[]){
    if(root->groups[0] != ROOT_USER){

        printf("not root user");
        exit(0);
    }

    User* current = root; 
    while(current){

        if (strcmp(current->id, id) == 0){

            strcpy(current->Name, new_name);
            current->age = new_age;

            for (int i = 0; i < MAXIMUM_GROUPS; i++) 
                current->groups[i] = new_groups[i];
            return;
        }
        current=current->nextUserPointer;
    }
    printf("User with this id %d is not found.\n", id);
}

void print_user(User* root, char* id) {
    if (root->groups[0] != ROOT_USER) {
        printf("Not root user\n");
        exit(0);
    }

    User* current = root;

    while (current) {
        if (strcmp(current->id, id) == 0) {
            printf("Name  %s\nAge  %d\nID  %s\nGroups  [ ", current->Name, current->age, current->id);
            for (int i = 0; i < MAXIMUM_GROUPS; i++) {

                if (current->groups[i] != GROUP_UNASSIGNED) 
                    printf("%s ", get_group_name(current->groups[i]));
                
            }
            printf("]\n");
            return;
        }
        current = current->nextUserPointer;
    }

    printf("User ID %s is not found.\n", id);
}



void add_group(User* root, char* id, Groups group){

    if(root->groups[0] != ROOT_USER){
        printf("not root user");
        exit(0);
    }

    User* current = root;
    while(current){
        if (strcmp(current->id, id) == 0){
            for(int i = 0; i<MAXIMUM_GROUPS; i++){
                if(current->groups[i] == GROUP_UNASSIGNED){
                    current->groups[i] = group;
                    return;
                }
            }
            fprintf(stderr, "User %s have max num of groups.\n", id);
            return;
        }
    }
}


void print_system(User* root){

    User* current = root;

    while(current){
        print_user(root, current->id);
        current=current->nextUserPointer;
    }
}

void create_user(User* root, char* name, int age, char* id, Groups groups[]) {

    if (root->groups[0] != ROOT_USER) {
        printf("not root user\n");
        exit(0);
    }

    User* new_user = (User*)malloc(sizeof(User));
    new_user->age = age;


    strncpy(new_user->Name, name, sizeof(new_user->Name) - 1);
    new_user->Name[sizeof(new_user->Name) - 1] = '\0';


    strncpy(new_user->id, id, sizeof(new_user->id) - 1);
    new_user->id[sizeof(new_user->id) - 1] = '\0';

   for (int i = 0; i < MAXIMUM_GROUPS; i++) {
        if (groups[i] == -1) {
            new_user->groups[i] = GROUP_UNASSIGNED; 
        } else if (groups[i] == ROOT_USER) {
            new_user->groups[i] = ROOT_USER;
        } else if (groups[i] >= SYSTEM_USERS && groups[i] < OPERATOR_USERS) {
            new_user->groups[i] = SYSTEM_USERS;
        } else if (groups[i] >= OPERATOR_USERS && groups[i] < OBSERVER_USERS) {
            new_user->groups[i] = OPERATOR_USERS;
        } else if (groups[i] >= OBSERVER_USERS) {
            new_user->groups[i] = OBSERVER_USERS;
        } else {
            new_user->groups[i] = GROUP_UNASSIGNED;
        }
    }
    new_user->nextUserPointer = NULL;

    User* current = root;
    while (current->nextUserPointer != NULL)
        current = current->nextUserPointer;
    
    current->nextUserPointer = new_user;
}


void export_system(User* root){

    FILE* fptr;

    fptr = fopen("output.txt", "w");
    fprintf(fptr, "User's id     user-Name      age    groups\n");

    User* current = root;

    while(current){

        fprintf(fptr,"%s %s %d [ ", current->id, current->Name, current->age);

        for (int i = 0; i < MAXIMUM_GROUPS; i++) {

            if (current->groups[i] != GROUP_UNASSIGNED) 
                fprintf(fptr, "%s ", get_group_name(current->groups[i]));
        }
        fprintf(fptr, "]\n");
        current = current->nextUserPointer;
    }

    fclose(fptr);
}
