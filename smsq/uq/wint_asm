; Write Integer routine  QL compatible    1989  Tony Tebby

        section uq

        xdef    uq_wint

        xref    uq_wtext
        xref    cr_iwdec

uq_wint
uwi.reg reg     a0/a1/a6
frame   equ     $10
stk_chan equ    $10
        movem.l uwi.reg,-(sp)
        sub.w   #frame,sp                ; put integer in stack frame
        move.l  sp,a0
        move.l  sp,a1
        move.w  d1,(a0)+                 ; word first, then charaters
        sub.l   a6,a6
        jsr     cr_iwdec
        sub.l   a1,a0                    ; length of string
        move.w  a0,-(a1)
        move.l  stk_chan(sp),a0          ; restore channel
        bsr.s   uq_wtext
        add.w   #frame,sp
        movem.l (sp)+,uwi.reg
        rts
        end
