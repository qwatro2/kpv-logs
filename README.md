[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/qwatro2/kpv-logs/blob/master/README.md)
[![pt-br](https://img.shields.io/badge/lang-ru-green.svg)](https://github.com/qwatro2/kpv-logs/blob/master/README.ru.md)

# Log analyzer

# How to use?

In IDEA, open the configuration settings

![image.png](images/image.png)

![image.png](images/image%201.png)

![image.png](images/image%202.png)

Here you can write command line arguments, after that we launch the project.

If the `--path` argument is missing, we will see an error

![image.png](images/image%203.png)

If the `--path` argument is not a path to local files or to a file on the Internet, we will see an error

![image.png](images/image%204.png)

If the `--from` and `--to` arguments are not in the `YYYY-MM-DD` format, we will see an error

![image.png](images/image%205.png)

If the `--format` argument is not `markdown` or `adoc`, we will see an error

![image.png](images/image%206.png)

If only one of the `--filter-field` and `--filter-value` arguments is specified, we will see an error

![image.png](images/image%207.png)

If the `--filter-field` argument specifies a field that is not in the logs, we will see an error

![image.png](images/image%208.png)

![image.png](images/image%209.png)

![image.png](images/image%2010.png)

If the `--output` argument contains `*` , we will see an error

![image.png](images/image%2011.png)

If the `--output` argument is not a local path, we will see an error

![image.png](images/image%2012.png)

If the `--output` argument leads to a file that cannot be created or opened, we will see an error

![image.png](images/image%2013.png)

Next, if there are no errors, the program will analyze the specified logs and output them in the format

- `markdown` or `adoc` if specified by the corresponding command line argument
- `plain text` if no argument is specified

  ![image.png](images/image%2014.png)

и

- to the console if the `--output` argument is not specified
- to the file specified by the `--output` argument

Metrics that count the number of requests for some feature display no more than 5 with the largest number of records.

# What's going on inside?

1. Parsing command line arguments
2. Validating these arguments
3. Getting `Stream` of log entries
4. Collecting statistics on these logs
5. Printing the collected statistics
