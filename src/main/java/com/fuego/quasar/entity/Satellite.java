package com.fuego.quasar.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Satellite implements Serializable {

    @Id
    private String id;
    private String name;
    private Double x;
    private Double y;
    private String state;
    @OneToMany
    private List<CodeMessages> messages;

    public static String[] fromStorage(List<CodeMessages> list){
        String[] messages = new String[list.size()];
        list.forEach(
                codeMessages ->
                        messages[Integer.parseInt(codeMessages.getId())] = codeMessages.getMessage()
        );
        return messages;
    }
}
