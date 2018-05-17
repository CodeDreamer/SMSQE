; VER$          V2.00      1990  Tony Tebby   QJUMP
;
;       VER$
;
        section exten

        xdef    ver$

        xref    ut_gxin1
        xref    ut_rtnam
        xref    ut_rtfd1
        xref    ut_retst
        xref    sb_vers

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sbasic'

ver$
        lea     sb_vers+1,a4             ; assume ver$
        sub.l   a6,a4                    ; rel(a6)

        cmp.l   a3,a5                    ; any params?
        beq.l   ut_rtnam                 ; ... no
        jsr     ut_gxin1                 ; one param required
        bne.s   ver_rts                  ; oops
        addq.l  #2,a1
        move.l  a1,sb_arthp(a6)          ; and remove from stack
        move.w  -2(a6,a1.l),d7           ; get code
        beq.l   ut_rtnam                 ; SBASIC version

        moveq   #sms.info,d0
        trap    #do.sms2                 ; get info
        addq.w  #1,d7
        bgt.s   ver_os                   ; OS version
        beq.l   ut_rtfd1                 ; JOB Number - float d1 and return
        move.l  a0,d1
        bra.l   ut_rtfd1                 ; sys var base

ver_os
        subq.l  #6,a1
        move.l  d2,2(a6,a1.l)            ; OS vers
        move.w  #4,(a6,a1.l)
        jmp     ut_retst                 ; return it

ver_rts
        rts
        end
