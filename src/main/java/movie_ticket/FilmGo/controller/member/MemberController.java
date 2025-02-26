package movie_ticket.FilmGo.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.member.dto.MemberForm;
import movie_ticket.FilmGo.controller.member.dto.MemberSearch;
import movie_ticket.FilmGo.converter.MemberConverter;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberConverter memberConverter;
    private final PasswordEncoder encoder;

    @GetMapping("/members/new")
    public String createMember(@ModelAttribute(name = "form") MemberForm form) {
        return "members/addMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Validated @ModelAttribute(name = "form") MemberForm form, BindingResult bindingResult) {
        if (memberService.findByName(form.getName()).isPresent()) {
            bindingResult.rejectValue("name", "nameError", "이미 존재하는 계정입니다.");
            return "members/addMemberForm";
        }

        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        Member member = memberConverter.toEntity(form);

        memberService.save(member);
        return "redirect:/";
    }

    @GetMapping("/members/update/{id}")
    public String updateMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("form", memberConverter.toForm(member));
        return "members/updateForm";
    }

    @PostMapping("/members/update/{id}")
    public String updateMember(@PathVariable Long id, @Validated @ModelAttribute(name = "form") MemberForm form, BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "members/updateForm";
        }
        Member member = memberService.findById(id);

        if (!encoder.matches(form.getCheckPassword(), member.getPassword())) {
            bindingResult.rejectValue("checkPassword", "현재 비밀번호가 일치하지 않습니다");
            return "members/updateForm";
        }

        memberService.update(member, form);
        return "redirect:/";
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id, Model model) {
        Member member = memberService.findById((id));
        model.addAttribute("member", member);
        return "members/memberForm";
    }

    @GetMapping("/members/list")
    public String findMemberList(Model model, MemberSearch memberSearch) {
        model.addAttribute("members", memberService.findAll(memberSearch));
        return "members/memberList";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Member member = memberService.deleteMember(id);
        redirectAttributes.addAttribute("memberId", id);
        return "redirect:/members/{memberId}";
    }
}
