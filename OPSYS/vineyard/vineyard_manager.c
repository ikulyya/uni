#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <dirent.h>
#include <errno.h>

#define DATA_DIR "data"
#define MAX_STR 128
#define MAX_LINE 512
#define MAX_RECORDS 2000

typedef struct {
    char plot[MAX_STR];
    char grape[MAX_STR];
    int area;
    char vineyard_type[MAX_STR];
    int damage;
} Record;

void trim_newline(char *s) {
    size_t len = strlen(s);
    if (len > 0 && s[len - 1] == '\n') {
        s[len - 1] = '\0';
    }
}

void read_line(const char *prompt, char *buffer, size_t size) {
    printf("%s", prompt);
    fflush(stdout);
    if (fgets(buffer, (int)size, stdin) == NULL) {
        buffer[0] = '\0';
        return;
    }
    trim_newline(buffer);
}

int read_int(const char *prompt) {
    char line[MAX_STR];
    int value;
    while (1) {
        read_line(prompt, line, sizeof(line));
        if (sscanf(line, "%d", &value) == 1) {
            return value;
        }
        printf("Invalid number, try again.\n");
    }
}

void get_first_word(const char *input, char *output, size_t out_size) {
    if (sscanf(input, "%127s", output) != 1) {
        output[0] = '\0';
    }
    output[out_size - 1] = '\0';
}

void build_location_path(const char *location, char *path, size_t path_size) {
    char first_word[MAX_STR];
    get_first_word(location, first_word, sizeof(first_word));
    snprintf(path, path_size, "%s/%s.txt", DATA_DIR, first_word);
}

int ensure_data_dir() {
    struct stat st;

    if (stat(DATA_DIR, &st) == -1) {
        if (mkdir(DATA_DIR, 0755) == -1) {
            perror("Cannot create data directory");
            return 0;
        }
    } else if (!S_ISDIR(st.st_mode)) {
        printf("A file named '%s' exists, but it is not a directory.\n", DATA_DIR);
        return 0;
    }

    return 1;
}

int parse_record_line(const char *line, Record *r) {
    return sscanf(line, "%127[^;];%127[^;];%d;%127[^;];%d",
                  r->plot, r->grape, &r->area, r->vineyard_type, &r->damage) == 5;
}

void print_record(const char *location, int index, const Record *r) {
    printf("[%d] Location: %s | Plot: %s | Grape: %s | Area: %d square fathoms | Type: %s | Damage: %d%%\n",
           index, location, r->plot, r->grape, r->area, r->vineyard_type, r->damage);
}

int load_records_from_location(const char *location, Record records[], int max_records) {
    char path[MAX_STR * 2];
    char content[MAX_RECORDS * MAX_LINE];
    char line[MAX_LINE];
    int fd, bytes_read, count = 0, i, line_pos = 0;
    ssize_t total = 0;

    build_location_path(location, path, sizeof(path));
    fd = open(path, O_RDONLY);
    if (fd == -1) {
        return -1;
    }

    while ((bytes_read = (int)read(fd, content + total, sizeof(content) - 1 - total)) > 0) {
        total += bytes_read;
        if (total >= (ssize_t)sizeof(content) - 1) {
            break;
        }
    }
    close(fd);

    if (bytes_read == -1) {
        perror("Read error");
        return -1;
    }

    content[total] = '\0';

    for (i = 0; i <= total; i++) {
        if (content[i] == '\n' || content[i] == '\0') {
            line[line_pos] = '\0';
            if (line_pos > 0 && count < max_records) {
                if (parse_record_line(line, &records[count])) {
                    count++;
                }
            }
            line_pos = 0;
        } else if (line_pos < MAX_LINE - 1) {
            line[line_pos++] = content[i];
        }
    }

    return count;
}

int save_records_to_location(const char *location, Record records[], int count) {
    char path[MAX_STR * 2], line[MAX_LINE];
    int fd, i;

    if (!ensure_data_dir()) return 0;

    build_location_path(location, path, sizeof(path));
    fd = open(path, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        perror("Cannot open file for saving");
        return 0;
    }

    for (i = 0; i < count; i++) {
        snprintf(line, sizeof(line), "%s;%s;%d;%s;%d\n",
                 records[i].plot, records[i].grape, records[i].area,
                 records[i].vineyard_type, records[i].damage);
        if (write(fd, line, strlen(line)) == -1) {
            perror("Write error");
            close(fd);
            return 0;
        }
    }

    close(fd);
    return 1;
}

void add_record() {
    char location[MAX_STR], path[MAX_STR * 2], file_key[MAX_STR], line[MAX_LINE];
    Record r;
    int fd;

    if (!ensure_data_dir()) return;

    read_line("Location: ", location, sizeof(location));
    read_line("Production site / plot: ", r.plot, sizeof(r.plot));
    read_line("Grape variety: ", r.grape, sizeof(r.grape));
    r.area = read_int("Area (square fathoms): ");
    read_line("Vineyard type: ", r.vineyard_type, sizeof(r.vineyard_type));
    r.damage = read_int("Damage percentage: ");

    get_first_word(location, file_key, sizeof(file_key));
    if (file_key[0] == '\0') {
        printf("Invalid location.\n");
        return;
    }

    build_location_path(location, path, sizeof(path));

    fd = open(path, O_WRONLY | O_CREAT | O_APPEND, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        perror("Cannot open file");
        return;
    }

    snprintf(line, sizeof(line), "%s;%s;%d;%s;%d\n",
             r.plot, r.grape, r.area, r.vineyard_type, r.damage);

    if (write(fd, line, strlen(line)) == -1) {
        perror("Write error");
    } else {
        printf("Record added successfully to %s\n", path);
        printf("Filename used: %s.txt\n", file_key);
    }

    close(fd);
}

void list_by_location() {
    char location[MAX_STR];
    Record records[MAX_RECORDS];
    int count, i;

    read_line("Location: ", location, sizeof(location));
    count = load_records_from_location(location, records, MAX_RECORDS);

    if (count == -1) {
        printf("No file found for this location.\n");
        return;
    }

    if (count == 0) {
        printf("No records in this location file.\n");
        return;
    }

    for (i = 0; i < count; i++) {
        print_record(location, i + 1, &records[i]);
    }
}

void list_all() {
    DIR *dir;
    struct dirent *entry;
    char location[MAX_STR];
    Record records[MAX_RECORDS];
    int count, i, any = 0;

    if (!ensure_data_dir()) return;

    dir = opendir(DATA_DIR);
    if (dir == NULL) {
        perror("Cannot open data directory");
        return;
    }

    while ((entry = readdir(dir)) != NULL) {
        size_t len = strlen(entry->d_name);
        if (strcmp(entry->d_name, ".") == 0 || strcmp(entry->d_name, "..") == 0) continue;
        if (len < 5 || strcmp(entry->d_name + len - 4, ".txt") != 0) continue;

        strncpy(location, entry->d_name, sizeof(location) - 1);
        location[sizeof(location) - 1] = '\0';
        location[len - 4] = '\0';

        count = load_records_from_location(location, records, MAX_RECORDS);
        if (count <= 0) continue;

        printf("\n=== %s ===\n", location);
        for (i = 0; i < count; i++) {
            print_record(location, i + 1, &records[i]);
        }
        any = 1;
    }

    if (!any) {
        printf("No records found.\n");
    }

    closedir(dir);
}

void list_by_grape() {
    DIR *dir;
    struct dirent *entry;
    char wanted[MAX_STR], location[MAX_STR];
    Record records[MAX_RECORDS];
    int count, i, found = 0;

    if (!ensure_data_dir()) return;

    read_line("Grape variety: ", wanted, sizeof(wanted));

    dir = opendir(DATA_DIR);
    if (dir == NULL) {
        perror("Cannot open data directory");
        return;
    }

    while ((entry = readdir(dir)) != NULL) {
        size_t len = strlen(entry->d_name);
        if (strcmp(entry->d_name, ".") == 0 || strcmp(entry->d_name, "..") == 0) continue;
        if (len < 5 || strcmp(entry->d_name + len - 4, ".txt") != 0) continue;

        strncpy(location, entry->d_name, sizeof(location) - 1);
        location[sizeof(location) - 1] = '\0';
        location[len - 4] = '\0';

        count = load_records_from_location(location, records, MAX_RECORDS);
        if (count <= 0) continue;

        for (i = 0; i < count; i++) {
            if (strcmp(records[i].grape, wanted) == 0) {
                print_record(location, i + 1, &records[i]);
                found = 1;
            }
        }
    }

    if (!found) {
        printf("No matching grape variety found.\n");
    }

    closedir(dir);
}

void modify_record() {
    char location[MAX_STR];
    Record records[MAX_RECORDS];
    int count, index, i;

    read_line("Location: ", location, sizeof(location));
    count = load_records_from_location(location, records, MAX_RECORDS);

    if (count <= 0) {
        printf("No records found for this location.\n");
        return;
    }

    printf("Records in %s:\n", location);
    for (i = 0; i < count; i++) {
        print_record(location, i + 1, &records[i]);
    }

    index = read_int("Record number to modify: ");
    if (index < 1 || index > count) {
        printf("Invalid record number.\n");
        return;
    }
    index--;

    printf("Enter new data:\n");
    read_line("Production site / plot: ", records[index].plot, sizeof(records[index].plot));
    read_line("Grape variety: ", records[index].grape, sizeof(records[index].grape));
    records[index].area = read_int("Area (square fathoms): ");
    read_line("Vineyard type: ", records[index].vineyard_type, sizeof(records[index].vineyard_type));
    records[index].damage = read_int("Damage percentage: ");

    if (save_records_to_location(location, records, count)) {
        printf("Record modified successfully.\n");
    }
}

void delete_record() {
    char location[MAX_STR], path[MAX_STR * 2];
    Record records[MAX_RECORDS];
    int count, index, i, new_count = 0;
    Record remaining[MAX_RECORDS];

    read_line("Location: ", location, sizeof(location));
    count = load_records_from_location(location, records, MAX_RECORDS);

    if (count <= 0) {
        printf("No records found for this location.\n");
        return;
    }

    printf("Records in %s:\n", location);
    for (i = 0; i < count; i++) {
        print_record(location, i + 1, &records[i]);
    }

    index = read_int("Record number to delete: ");
    if (index < 1 || index > count) {
        printf("Invalid record number.\n");
        return;
    }
    index--;

    for (i = 0; i < count; i++) {
        if (i != index) {
            remaining[new_count++] = records[i];
        }
    }

    if (new_count == 0) {
        build_location_path(location, path, sizeof(path));
        if (unlink(path) == -1) {
            perror("Could not delete empty location file");
        } else {
            printf("Record deleted and empty location file removed.\n");
        }
    } else if (save_records_to_location(location, remaining, new_count)) {
        printf("Record deleted successfully.\n");
    }
}

void print_menu() {
    printf("\n--- Vineyard Infection Manager ---\n");
    printf("1. Add new record\n");
    printf("2. Modify record\n");
    printf("3. Delete record\n");
    printf("4. List all records\n");
    printf("5. List records by location\n");
    printf("6. List records by grape variety\n");
    printf("0. Exit\n");
}

int main() {
    int choice;

    if (!ensure_data_dir()) {
        return 1;
    }

    while (1) {
        print_menu();
        choice = read_int("Choice: ");

        switch (choice) {
            case 1:
                add_record();
                break;
            case 2:
                modify_record();
                break;
            case 3:
                delete_record();
                break;
            case 4:
                list_all();
                break;
            case 5:
                list_by_location();
                break;
            case 6:
                list_by_grape();
                break;
            case 0:
                printf("Goodbye!\n");
                return 0;
            default:
                printf("Invalid menu option.\n");
        }
    }

    return 0;
}
