; Initialise the Procedure Thing   V1.00     1990   Tony Tebby   QJUMP

        xdef    pr_init
        xdef    prc_name

        xref.l  pr_vers
        xref    gu_thzlk
        xref    gu_achpp

        xref    pr_addp
        xref    pr_remp
        xref    pr_find
        xref    pr_mkls
        xref    pr_usep

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'
        include 'dev8_mac_assert'
        include 'dev8_ee_procs_data'

        section version
prc_name dc.w   10,'Procedures V'
prc.name equ    12
prc_vers dc.l   pr_vers
         dc.w   $200a

         section procs

prc_tab
        dc.w    pr_addp-*
        dc.w    pr_remp-*
        dc.w    pr_find-*
        dc.w    pr_mkls-*
        dc.w    pr_usep-*
        dc.w    pri_nimp-*
        dc.w    pri_nimp-*
        dc.w    pri_nimp-*
        dc.w    0

pri_nimp
        moveq   #err.nimp,d0
        rts

;+++
; Initialise Procedures Thing
;---
pr_init
pri.reg reg     a0/a1/a2/a3
        movem.l pri.reg,-(sp)

        move.l  #th_name+prc.name+prc_end,d0 ; allocate thing and link
        jsr     gu_achpp
        bne.s   pri_exit
        lea     th_name+prc.name(a0),a1 ; thing itself
        move.l  a1,th_thing(a0)          ; pointer to thing
        st      th_nshar(a0)             ; not shareable
        assert  th_verid,th_name-4
        lea     th_verid(a0),a1
        move.l  prc_vers,(a1)+           ; version
        lea     prc_name,a2
        assert  prc.name,12
        move.l  (a2)+,(a1)+              ; and name
        move.l  (a2)+,(a1)+
        move.l  (a2)+,(a1)+

        move.l  #thh.flag,(a1)           ; flag
        add.w   #prc_addp-thh_flag,a1    ; vectors
        lea     prc_tab,a2

pri_vect
        move.l  a2,a3
        move.w  (a2)+,d0                 ; next vector
        beq.s   pri_link
        add.w   d0,a3
        move.w  #$4ef9,(a1)+             ; JMP
        move.l  a3,(a1)+                 ; absolute address
        bra.s   pri_vect


pri_link
        moveq   #sms.lthg,d0             ; link in
        jsr     gu_thzlk

pri_exit
        movem.l (sp)+,pri.reg
        rts
        end
