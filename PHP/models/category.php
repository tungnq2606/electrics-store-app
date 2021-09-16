<?php
class Category
{
    private $id;
    private $category_name;
    private $image_url;


    function __construct($id, $category_name, $image_url)
    {
        $this->id = $id;
        $this->category_name = $category_name;
        $this->image_url = $image_url;
    }

    public function getId()
    {
        return $this->id;
    }
    public function getCategory_name()
    {
        return $this->category_name;
    }
    public function getImage_url()
    {
        return $this->image_url;
    }
}
