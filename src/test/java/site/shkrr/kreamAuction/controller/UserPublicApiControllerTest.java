package site.shkrr.kreamAuction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import site.shkrr.kreamAuction.controller.user.UserPublicApiController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.shkrr.kreamAuction.ApiDocumentUtils.getDocumentRequest;
import static site.shkrr.kreamAuction.ApiDocumentUtils.getDocumentResponse;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class UserPublicApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

   @Autowired
   private UserPublicApiController userPublicApiController;


    //휴대폰 인증 발급
    @Test
   public void issuePhoneCertification() throws Exception {
       Map<String,String> map=new HashMap<>();
       map.put("phoneNum","01039229957");
        System.out.println(objectMapper.writeValueAsString(map));
       ResultActions result =mockMvc.perform(post("/user/public/sms")
               .content(objectMapper.writeValueAsString(map))
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

       result.andExpect(status().isOk())
               .andDo(document("issuePhoneCertification", getDocumentRequest(),getDocumentResponse(),
                       requestFields(fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화 번호")),
                        responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("메세지"))
               ));
   }
}