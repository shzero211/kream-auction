package site.shkrr.kreamAuction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.controller.user.UserPublicApiController;
import site.shkrr.kreamAuction.service.certification.SmsCertificationService;

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

   @Autowired
   private SmsCertificationService smsCertificationService;

    //휴대폰 인증번호 발급 테스트
    @Test
    @DisplayName("휴대폰 인증번호 발급 테스트")
   public void smsPhoneCertification() throws Exception {
       Map<String,String> map=new HashMap<>();
       map.put("phoneNum","01039229957");
       ResultActions result =mockMvc.perform(post("/user/public/sms")
               .content(objectMapper.writeValueAsString(map))
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

       result.andExpect(status().isOk())
               .andDo(document("issueCertification", getDocumentRequest(),getDocumentResponse(),
                       requestFields(fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호")),
                        responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("메세지"))
               ));
   }

   @Test
   @DisplayName("휴대폰 인증번호 인증")
    public void smsPhoneCertificationVerify() throws Exception {
        String phoneNum="01039229957";
        String certificationNum=smsCertificationService.sendTo(phoneNum);//인증 번호 발급

       UserDto.UserSmsConfirmRequestDto requestDto=UserDto.UserSmsConfirmRequestDto.builder()
               .phoneNum(phoneNum)
               .certificationNum(certificationNum)
               .build();

       ResultActions result=mockMvc.perform(
               post("/user/public/sms/confirm")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(requestDto))
                       .accept(MediaType.APPLICATION_JSON));

       result.andExpect(status().isOk())
               .andDo(
                       document("issueCertificationVerify",getDocumentRequest(),getDocumentResponse(),
                           requestFields(
                                   fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호"),
                                    fieldWithPath("certificationNum").type(JsonFieldType.STRING).description("휴대폰인증번호")
                           ),
                           responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("메세지")
                           )
                       )
               );
   }
}