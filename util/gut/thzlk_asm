; Zap and Link Thing HOTKEY System II

        section gen_util

        xdef    gu_thzlk

        xref    gu_thvec

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_thg'
;+++
; Zap and link Thing through HOTKEY System II.
; Note this only works if a HOTKEY System version 2.03 or later is present.
;
;                Entry                           Exit
;       a0       thing linkage                   preserved
;
;       Condition codes set
;---
gu_thzlk
gtz.reg reg     d2/a0/a1/a2/a4
stk_a0  equ     $04
        movem.l gtz.reg,-(sp)
        moveq   #thh_entr,d0            ; thing vector required
        bsr.s   gu_thvec                ; get THING vector
        bne.s   gtz_exit                ; there's nothing to jump to!
        lea     th_name(a0),a0          ; name
        moveq   #sms.zthg,d0
        jsr     (a4)                    ; zap it
        move.l  stk_a0(sp),a1
        moveq   #sms.lthg,d0            ; link it
        jsr     (a4)
gtz_exit
        movem.l (sp)+,gtz.reg
        rts
        end
