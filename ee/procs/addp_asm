; Add Procedures to the Procedure Thing   V1.00     1990   Tony Tebby   QJUMP

        section procs

        xdef    pr_addp

        xref    pr_upcpy
        xref    gu_adlis

        include 'dev8_keys_err'
        include 'dev8_mac_assert'
        include 'dev8_ee_procs_data'

;+++
; Add Procedures to  Procedures Thing
;
;       a1 c  p pointer to Thing
;       a2 c  p pointer to procedure table
;---
pr_addp
pra.reg reg     d2/a0/a1/a2/a3/a4
        movem.l pra.reg,-(sp)
        move.l  a1,a4                    ; procedures Thing
        move.l  a2,a3
        add.l   (a2)+,a3
        move.w  (a3),d0                  ; Extension Thing name?
        beq.s   pra_inam                 ; ... no
        cmp.w   #prc.mxtn,d0             ; name too long?
        bhi.s   pra_inam                 ; ... yes
        moveq   #prc.thls,d0
        moveq   #0,d2
        move.l  prc_thgl(a4),a1
        lea     pra_thng,a0
        jsr     gu_adlis                 ; add thing to list
        move.l  a1,prc_thgl(a4)
        tst.l   d0
        bne.s   pra_exit

        moveq   #prc.prls,d0
        move.l  prc_prcl(a4),a1
        lea     pra_proc,a0
        jsr     gu_adlis                 ; add procedures to list
        move.l  a1,prc_prcl(a4)
        tst.l   d0
        bne.s   pra_exit
        tst.w   (a2)                     ; ... complete?
        bne.s   pra_inam                 ; ... no

pra_exit
        movem.l (sp)+,pra.reg
        rts
pra_inam
        moveq   #err.inam,d0
        bra.s   pra_exit

;+++
; Add Extension Thing to list
;
;       d2
;       a3 cr   pointer to thing name / pointer to thing name in list
;
;---
pra_thng
        move.l  d2,d0                    ; re-entry?
        bne.s   pra_eof                  ; ... yes

        exg     a1,a3                    ; entry pointer in a3

        move.l  a2,d2                    ; save table pointer (and smash d2)
        move.l  a3,a2                    ; .... because we'll be copying
        move.w  (a1)+,d0
        move.w  d0,(a2)+
pat_loop
        move.w  (a1)+,(a2)+              ; copy characters in words
        subq.w  #2,d0
        bgt.s   pat_loop

        move.l  d2,a2                    ; restore table pointer
        move.l  a3,a1                    ; and entry base
        moveq   #0,d0
pra_rts
        rts
;+++
; Add Procedure to list
;
;       a2 c  u pointer to procedure table
;       a3 c    pointer to thing name in list
;
;---
pra_proc
        assert  prc_thpt,prc_exid-4,prc_prnm-8,0
        move.l  a3,(a1)+                 ; set pointer to thing
        move.l  (a2)+,(a1)+              ; and extension ID
        beq.s   pap_eof2                 ; ... none

        moveq   #prc.mxpn,d1
        jsr     pr_upcpy                 ; copy uppercase
        blt.s   pap_eof                  ; name too long (a1 not updated)

        sub.w   #prc.mxpn+prc_prch,a1    ; backspace a1
        moveq   #0,d0
        rts

pap_eof2
        subq.l  #4,a2                    ; backspace a2
pap_eof
        subq.l  #8,a1                    ; restore entry base

pra_eof
        moveq   #err.eof,d0
        rts
        end
