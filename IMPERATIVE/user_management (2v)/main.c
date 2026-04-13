#include <stdio.h>
#include "UserManagementSystem.c"


int main() {

    User* root_User = init();

    int ch;



    do {

        printf("\nMenu:\n");
        printf(" 1) count users\n");
        printf(" 2) print user\n");
        printf(" 3) create user\n");
        printf(" 4) modify user\n");
        printf(" 5) delete user\n");
        printf(" 6) add a group to user\n");
        printf(" 7) print system\n");
        printf(" 8) export system\n");
        printf(" 0) exit\n");
        printf("Write your number:");

        scanf("%d", &ch);

        switch (ch) {

            case 1: {

                printf("Total num of all users: %d\n", count_users(root_User));

                break;

            }
            case 2: {
                char id[25];

                printf("Write user's id: ");

                scanf("%s", id);

                print_user(root_User, id);
                break;
            }
            case 3: {
                int age;
                char name[60], id[25];
                Groups groups[MAXIMUM_GROUPS] = {GROUP_UNASSIGNED, GROUP_UNASSIGNED, GROUP_UNASSIGNED, GROUP_UNASSIGNED};
                printf("Write Name , Age , id , and max 4 groups , if unassigned then -1  : ");
                scanf("%s %d %s", name, &age, id);
                for (int i = 0; i < MAXIMUM_GROUPS; i++) {

                    scanf("%d", (int*)&groups[i]);
                }

                create_user(root_User, name, age, id, groups);

                break;
            }

            case 4: {
                int new_age;
                char id[25], new_name[60];
                

                Groups new_groups[MAXIMUM_GROUPS] = {GROUP_UNASSIGNED, GROUP_UNASSIGNED, GROUP_UNASSIGNED, GROUP_UNASSIGNED};

                printf("Write id, new name, new age, and max 4 groups , if unassigned -1: ");
                scanf("%s %s %d", id,  new_name, &new_age);
                for (int i = 0; i < MAXIMUM_GROUPS; i++) {

                    scanf("%d", (int*)&new_groups[i]);
                }
                modify_user(root_User, id, new_name, new_age, new_groups);
                break;
            }
            case 5: {
                char id[25];

                printf("Write user's id: ");

                scanf("%s", id);


                delete_user(root_User, id);

                break;
            }
            case 6: {
                char id[25];
                Groups group;

                printf("Write user's id and group: ");

                scanf("%s %d", id, (int*)&group);

                add_group(root_User, id, group);

                break;
            }
            case 7: {

                print_system(root_User);

                break;
            }
            case 8: {

                export_system(root_User);

                break;
            }
            case 0:

                printf("Exit program.\n");

                break;

            default:

                printf("Invalid Choice.\n");
        }
    } while (ch != 0);


    free_users(root_User);

    return 0;
}
