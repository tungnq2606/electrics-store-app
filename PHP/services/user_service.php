<?php
include_once '../configs/database_config.php';
include_once '../models/user.php';
include_once '../models/reset.php';


class UserService
{
    private $connection;
    private $tblUsers = "tblUsers";
    private $tblPasswordResets = "tblPasswordResets";

    // create constructor

    public function __construct()
    {
        $this->connection = (new DatabaseConfig())->getConnection();
    }

    // register new user

    public function register($user)
    {
        try {
            $query = "INSERT INTO " . $this->tblUsers . " 
                                        SET email = :email, avatar = null, fullname = :fullname, hash_password = :hash_password
                                        ";
            $stmt = $this->connection->prepare($query);

            $email = $user->getEmail();
            $password = $user->getHashPassword();
            $fullname = $user->getFullname();
            $stmt->bindParam(":email", $email);
            $stmt->bindParam(":fullname", $fullname);
            $stmt->bindParam(":hash_password", $password);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // get user by email address

    public function getUserByEmail($email)
    {
        try {
            $query = "SELECT id, email, avatar, fullname, hash_password FROM " . $this->tblUsers . " 
                                        WHERE email = ? LIMIT 0,1";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $email);
            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                extract($row);
                return new User($id, $email, $avatar, $fullname, $hash_password);
            }
        } catch (Exception $e) {
        }
        return null;
    }

    // change password

    function changePassword($email, $hash_password)
    {
        try {
            $query = "UPDATE " . $this->tblUsers . " 
                                        SET hash_password = :hash_password WHERE email = :email";
            $stmt = $this->connection->prepare($query);
            $stmt->bindParam(":email", $email);
            $stmt->bindParam(":hash_password", $hash_password);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // create token from password and save to resetpassword table

    public function generateResetToken($email)
    {
        $token = md5($email) . rand(10, 9999);
        try {
            $q = "insert into " . $this->tblPasswordResets . "
                            set email=:email, token=:token";
            $stmt = $this->connection->prepare($q);
            // bind data
            $stmt->bindParam(":email", $email);
            $stmt->bindParam(":token", $token);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return $token;
            } else {
                $this->connection->rollBack();
                return null;
            }
        } catch (Throwable $th) {
        }
        return null;
    }

    // clear token

    public function clearToken($email)
    {
        try {
            $q = "DELETE FROM tblPasswordResets WHERE email= :email";
            $stmt = $this->connection->prepare($q);
            // bind data
            $stmt->bindParam(":email", $email);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (\Throwable $th) {
        }
        return false;
    }

    // get user by token

    public function getByToken($token, $email)
    {
        try {
            $query = "SELECT id FROM " . $this->tblPasswordResets . " 
                                WHERE email = ? 
                                and token = ?
                                and available = 1
                                and created > now() - interval 10 minute    
                                LIMIT 0,1";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $email);
            $stmt->bindParam(2, $token);
            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                return true;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // get count token by email address

    public function getCountTokenByEmail($email)
    {
        try {
            $query = "SELECT COUNT(*) AS sum FROM tblpasswordresets WHERE created > now() - INTERVAL 40 minute";
            $stmt = $this->connection->prepare($query);

            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                return $row["sum"];
                // extract($row);
                // return new Reset($id, $email, $token, $created, $available);
            }
        } catch (Exception $e) {
        }
        return null;
    }
}
