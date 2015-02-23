using System;
using System.Collections;
using System.Data.SqlClient;
using System.Text;
using System.Security.Cryptography;

namespace ConvertPasswordToBase64
{
    class Program
    {

        static string readpass()
        {
            string pass = "";
            ConsoleKeyInfo key;
            do
            {
                key = Console.ReadKey(true);
                if (key.Key >= ConsoleKey.Spacebar) 
                {
                    pass += key.KeyChar;
                    Console.Write("*");                    
                }
                else if (key.Key == ConsoleKey.Backspace)
                {
                    pass = pass.Substring(0, pass.Length - 1);
                    Console.Write("\b");                        
                }
            }            
            while (key.Key != ConsoleKey.Enter);
            return pass;
        }

        static void Main(string[] args)
        {

            string server = "127.0.0.1";
            string instance = "SQLEXPRESS";
            string database = "backenddev";
            string replaceconn = "";

            Hashtable passlist = new Hashtable();

            for (int i = 0; args != null && i < args.Length; i++)
            {
                if (args[i].ToLower().Equals("/server"))
                {
                    server = args[++i];
                }
                else if (args[i].ToLower().Equals("/instancename"))
                {
                    instance = args[++i];
                }
                else if (args[i].ToLower().Equals("/database"))
                {
                    database = args[++i];
                }
                else if (args[i].ToLower().Equals("/connectionstring"))
                {
                    replaceconn = args[++i];
                }
                else if (args[i].ToLower().Equals("/adduser"))
                {
                    string user = args[++i];
                    string pass = null;

                    for (int j = 1; j <= 5; j ++)
                    {
                        Console.Clear();
                        if (j > 1) Console.WriteLine("Warning: passwords not matching! retry nr. {0} ...", j);
                        Console.Write("Enter password for user '{0}': ", user);
                        string secret1 = readpass();
                        Console.WriteLine();
                        Console.Write("confirm password: ");
                        string secret2 = readpass();
                        if (secret2.Equals(secret1))
                        {
                            pass = secret2;
                            break;
                        }
                        if (i == 5)
                        {
                            Console.WriteLine();
                            Console.WriteLine("Sei veramente un coglione!");
                            Console.ReadKey();
                            return;
                        }
                    }
                    byte[] tmpSource = ASCIIEncoding.ASCII.GetBytes(pass);  //converts the arry into Asci
                    byte[] tmpHash = new MD5CryptoServiceProvider().ComputeHash(tmpSource); 
                    passlist.Add(user, Convert.ToBase64String(tmpHash));

                }

            }

            string connectionstring = (replaceconn.Length == 0 ?
                    "Data Source=" + server + @"\" + instance + ";Initial Catalog=" + database + ";Integrated Security=SSPI"
                    : replaceconn);

            string queryString = "SELECT userId, password FROM dbo.Users;";

            Console.WriteLine();
            Console.WriteLine("connecting SQL Server with connectionstring set to {0} ...", connectionstring);
         
            using (SqlConnection connection =
                       new SqlConnection(connectionstring))
            {

                connection.Open();

                if (passlist.Count == 0)
                {
                    using (SqlCommand command = new SqlCommand(queryString, connection))
                    {
                        SqlDataReader reader = command.ExecuteReader();
                        while (reader.Read())
                        {
                            string user = reader.GetString(0);
                            byte[] d = ASCIIEncoding.ASCII.GetBytes(reader.GetString(1));

                            string pass = null;

                            for (int i = 1; i <= 5; i++)
                            {
                                Console.Clear();
                                if (i > 1) Console.WriteLine("Warning: passwords not matching! retry nr. {0} ...", i);
                                Console.Write("Enter password for user '{0}': ", user);
                                string secret1 = readpass();
                                Console.WriteLine();
                                Console.Write("confirm password: ");
                                string secret2 = readpass();
                                if (secret2.Equals(secret1))
                                {
                                    pass = secret2;
                                    break;
                                }
                                if (i == 5)
                                {
                                    Console.WriteLine();
                                    Console.WriteLine("Sei veramente un coglione!");
                                    Console.ReadKey();
                                    return;
                                }
                            }

                            byte[] tmpSource = ASCIIEncoding.ASCII.GetBytes(pass);  //converts the arry into Asci
                            byte[] tmpHash = new MD5CryptoServiceProvider().ComputeHash(tmpSource);
                            passlist.Add(user, Convert.ToBase64String(tmpHash));
                        }
                        reader.Close();
                    }
                }
                else 
                {
                    foreach (string userId in passlist.Keys) 
                    {
                        using (SqlCommand update = new SqlCommand(
                                "insert into users (userId, role) values ('" + userId + "', 'Administrator')",
                                connection))
                        {
                            //Console.Write("saving base64 password's hashcode for user {0} ...", userId);
                            //Console.WriteLine(" ==> {0}", passlist[userId]);
                            update.ExecuteNonQuery();                        
                            Console.WriteLine("added user {0} ...", userId);
                        }
                    }
                }

                Console.Clear();
                Console.WriteLine("updating database ...");
                int count = 0;
                foreach (string userId in passlist.Keys)
                {
                    using (SqlCommand update = new SqlCommand(
                        "update users set password = '" + passlist[userId] + "' where userId = '" + userId + "'",
                        connection))
                    {
                        //Console.Write("saving base64 password's hashcode for user {0} ...", userId);
                        //Console.WriteLine(" ==> {0}", passlist[userId]);
                        update.ExecuteNonQuery();
                        count++; 
                        Console.WriteLine("{0}) changed password for user {1}.", count, userId);
                    }
                }

                Console.WriteLine("saved password for {0} users, press a key to exit...", count);
                Console.ReadKey();

            }    
        }

    }
}
