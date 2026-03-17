package za.co.entelect.java_devcamp.customerdto;

public class DocumentDto {

    private Integer Id;
    private String Name;
    private String Document;

    public DocumentDto(){}

    public DocumentDto(Integer id, String name, String document){
        this.Id = id;
        this.Name = name;
        this.Document = document;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        Document = document;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
