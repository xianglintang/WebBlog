package com.tangxianglin.blog.mapper;



import com.tangxianglin.blog.pojo.MailMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {

    void InsertMailMessage(MailMessage mailMessage);

}
