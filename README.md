# Secure File System App
### This app can marshal and unmarshal json files containing arrays of file objects like this:
```
[
  {
    "id" : 23,
    "filename": "example_document.pdf",
    "size": 5120,
    "extension": "pdf",
    "language": "English",
    "year_published": 2023,
    "owner": "John Doe"
  }
]
```
### The main functionality of the app is concurrent parsing of a folder with JSON files containing objects of the specified structure mentioned above and creating a statistics by certain JSON property in XML file.
#### Notice! The app considers that every json file is valid and corresponds to the particular structure. Otherwise, the program will end up in an infinite loop. Detailed examples of the folders of JSON files can be seen in: 
- [Json files examples](src/main/resources/files_json)
- [Json files for an experiment with multi-threading](src/main/resources/json_files_for_experiment)

### To start using the application, clone the repository and create a .jar file of the project. Further, in your command line execute a newly created .jar archive with the following params:

- `folderPath` - a source folder that contains JSON files
- `value` - a property of file objects the parser should create statistics of. Can have such values: `language`, `size`, `owner`, `year_published`, `extension`
- `xmlPath` - a destination folder of future statistics_by_%s.xml file
- `threadNum` - a non-mandatory param, a number of threads the program will use. Must be greater than 0 and be a whole number. Default value is 2.

### The execution of a .jar file can be as following (if it's Windows):

- `java -jar .\yourJarDirectory\SecureFileSystem.jar "C:\sourceFolder" language "C:\destinationFolder" 4`
### If everything goes well we might see a possible result of the execution in a destination folder:
***statistics_by_language.xml***
```
<statistics>
    <item>
        <value>English</value>
        <count>4</count>
    </item>
    <item>
        <value>Ukrainian</value>
        <count>4</count>
    </item>
    <item>
        <value>French</value>
        <count>3</count>
    </item>
    <item>
        <value>Spanish</value>
        <count>3</count>
    </item>
    <item>
        <value>Russian</value>
        <count>2</count>
    </item>
</statistics>
```

## Project Entities

#### The business logic of the app contains two main entities in relation One-To-Many: [User](src/main/java/io/vital/app/entities/User.java) & [File](src/main/java/io/vital/app/entities/File.java), but in this project we work only with files. 
#### Secondary entities for implementing business functionality were created such as: [Statistics](src/main/java/io/vital/app/entities/Statistics.java) & [Item](src/main/java/io/vital/app/entities/Item.java). Statistics class contains Item entities which in their turn have counters of the numbers of occurrences of some property values in json files. The also have a relation One-To-Many.

## Multithreaded execution experiment
### As was mentioned before, the application uses multithreaded environment to parse a folder with files faster but is it really faster on practice? Let's find out!
#### The experiment uses the folder:
- ***[Json files for an experiment with multi-threading](src/main/resources/json_files_for_experiment)***

#### The folder contains 8 files, which means, theoretically, the program executes the fastest when we use 8 threads max, if more threads are used the excessive ones are just not used by the program, because for each file a separate thread is reserved. So for the experiment we decided to take 1, 2, 4, and 8 threads to execute the program. Let's see how much time the execution does take for each number of threads:
- `1 thread` - 0.031 sec
- `2 threads` - 0,025 sec
- `4 threads` - 0,026 sec
- `8 threads` - 0,032 sec

#### According to the results we can see that the biggest number of threads led us to the worst execution time, meanwhile 2 threads have the best one, but why? The answer is simple. If our program didn't have a shared resource between threads as [Statistics](src/main/java/io/vital/app/entities/Statistics.java) object like in our case, 8 threads simultaneously would execute the program the fastest, however, if we have a shared resource, our execution is not completely asynchronous and threads enter some sections of code that should be synchronised to avoid Data Race. By doing so, it increases the awaiting time of some threads to proceed their execution, and the more threads we have in our programs, the bigger awaiting time becomes between threads and greater number of threads becomes just a burden and redundancy for our program. That's why 2 threads showed the best execution time and for our experiment this amount of threads turned out to be the most efficient one. However, an efficient amount of threads depends on the file size, number of files and so on, so we have to experiment always.  





