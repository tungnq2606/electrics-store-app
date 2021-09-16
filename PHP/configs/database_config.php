<?php
class DatabaseConfig
{
    private $host = "185.27.134.10";
    private $username = "";
    private $password = "";
    private $database_name = "epiz_29674172_ReviewApp";

    public $connection;
    public function getConnection()
    {
        $this->connection = null;
        try {
            $this->connection = new PDO(
                "mysql:host=" . $this->host .
                    "; dbname=" . $this->database_name,
                $this->username,
                $this->password
            );
            $this->connection->exec("set names utf8");
        } catch (Exception $e) {
            echo "Error" . $e->getMessage();
        }
        return $this->connection;
    }
}
