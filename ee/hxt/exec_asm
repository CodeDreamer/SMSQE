; Procedure to set up execute hotkeys  V2.00     1990   Tony Tebby   QJUMP

        section hotkey

        xdef    hxt_res
        xdef    hxt_resw
        xdef    hxt_trn
        xdef    hxt_trnw
        xdef    hxt_exec
        xdef    hxt_wake
        xdef    hxt_ldex
        xdef    hxt_ldwk

        xref    hxt_repl
        xref    hxt_mkxi
        xref    hk_dflts
        xref    hk_newst

        xref    hxt_prmk

        xref    gu_hkuse
        xref    gu_hkfre
        xref    gu_thjmp
        xref    gu_achpp
        xref    gu_rchp
        xref    gu_fopen
        xref    gu_fclos
        xref    gu_iowp

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_err'
        include 'dev8_keys_hdr'
        include 'dev8_keys_thg'
        include 'dev8_ee_hk_xhdr'
        include 'dev8_ee_hk_data'
        include 'dev8_ee_hxt_data'
        include 'dev8_mac_thg'

max.name equ    $24                      ; max length of program name
alc.name equ    max.name+4               ; space allocated for max name

;+++
; HOT_RES filename \P parameters \J Job name \W Wake name
;---
hxt_res thg_extn {RES },hxt_resw,hxt_prmk

hex.reg reg     d1/d2/d3/d6/a0/a1/a2/a3/a4
stk_parm equ    $14
        movem.l hex.reg,-(sp)
        moveq   #hki.xthg,d6             ; execute thing
        bra.s   hex_file

;+++
; HOT_RESW filename \P parameters \J Job name \W Wake name
;---
hxt_resw thg_extn {RESW},hxt_trn,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.wake,d6             ; wake thing
        bra.s   hex_file

;+++
; HOT_TRN filename \P parameters \J Job name \W Wake name
;---
hxt_trn thg_extn {TRN },hxt_trnw,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.xttr,d6             ; execute thing (transient)
        bra.s   hex_file

;+++
; HOT_TRN filename \P parameters \J Job name \W Wake name
;---
hxt_trnw thg_extn {TRNW},hxt_exec,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.wktr,d6             ; wake thing (transient)
hex_file
        jsr     hxt_repl                 ; replace Hotkey
        bne.l   hex_exit

        move.l  hxt_itnm(a1),a0          ; file name

        moveq   #ioa.kshr,d3             ; shared
        lea     gu_fopen,a4              ; open
        pea     -$70(sp)                 ; workspace
        jsr     hk_dflts                 ; with defaults
        move.l  (sp)+,a1
        bne.l   hex_exit

        moveq   #iof.rhdr,d0             ; read header
        moveq   #hdr.set,d2              ; minimum
        jsr     gu_iowp
        bne.l   hex_fclos                ; ... oops, close file

        cmp.b   #1,hdr_type(a1)          ; executable?
        bne.l   hex_ijob                 ; ... no
        move.l  hdr_flen(a1),d2          ; file length
        move.l  hdr_data(a1),d3          ; data space

        move.l  a0,a4
        moveq   #(th.len+thh_strt+4+hkh.hlen)/2+alc.name,d0
        add.w   d0,d0                    ; length of thing
        move.l  d0,a1                    ; start of program
        add.l   d2,d0                    ; plus length of program
        jsr     gu_achpp
        bne.l   hex_fclos                ; oops
        add.l   a0,a1                    ; abs start of program
        move.l  a0,a2                    ; base of thing

        moveq   #iof.load,d0             ; load file
        move.l  a4,a0
        jsr     gu_iowp
        jsr     gu_fclos                 ; finished with the file now
        bne.l   hex_rthg                 ; oops, return heap

        move.l  stk_parm(sp),a0          ; use job name
        move.l  hxt_jbnm(a0),d0
        beq.s   hex_pnam                 ; ... none
        move.l  d0,a4
        move.w  (a4),d0                  ; length of job name
        beq.s   hex_pnam                 ; ... none
        cmp.w   #max.name,d0             ; too long?
        bls.s   hex_setth                ; ... no

hex_pnam
        lea     6(a1),a4                 ; program header
        cmp.w   #hkh.flag,(a4)+          ; flagged?
        bne.s   hex_fnam                 ; ... no, use filename
        move.w  (a4),d0                  ; length of program name
        beq.s   hex_fnam                 ; ... none
        cmp.w   #max.name,d0             ; too long?
        bls.s   hex_setth                ; ... no

hex_fnam
        move.l  hxt_itnm(a0),a4          ; use item name

hex_setth
        lea     th_name(a2),a0           ; fill in name
        move.l  a4,a3                    ; keep pointer to name

        move.w  (a3)+,d0
        move.w  d0,(a0)+
hex_setn
        move.w  (a3)+,(a0)+              ; create thing name
        subq.w  #2,d0
        bgt.s   hex_setn

        lea     th_name+alc.name(a2),a0
        move.l  a0,th_thing(a2)          ; set pointer to thing
        move.l  #thh.flag,(a0)+          ; flag
        moveq   #tht.exec,d0             ; type
        move.l  d0,(a0)+
        moveq   #thh_strt+4,d0           ; start of header
        move.l  d0,(a0)+
        moveq   #hkh.hlen+max.name+2,d0  ; length of header
        move.l  d0,(a0)+
        move.l  d3,(a0)+                 ; data space
        moveq   #thh_strt+4,d0
        move.l  d0,(a0)+                 ; start of code

        move.w  #hkh.jmpl,(a0)+
        move.l  a1,(a0)+
        move.w  #hkh.flag,(a0)+

        move.l  a4,a3
        move.w  (a3)+,d0
        move.w  d0,(a0)+
hex_setp
        move.w  (a3)+,(a0)+              ; create program name
        subq.w  #2,d0
        bgt.s   hex_setp

        move.l  a2,a1                    ; set thing base
        moveq   #sms.lthg,d0             ; link in thing
        jsr     gu_thjmp
        move.l  stk_parm(sp),a1          ; restore parameter pointer
        beq.l   hex_addi                 ; thing added, carry on

hex_rthg
        move.l  a2,a0
        jsr     gu_rchp                  ; can't do it, remove our bit of thing
        bra.l   hex_exit
hex_ijob
        moveq   #err.ijob,d0             ; invalid job
hex_fclos
        jsr     gu_fclos
        bra.l   hex_exit

;+++
; HOT_EXEC filename \P parameters \J Job name \W Wake name
;---
hxt_exec thg_extn {EXEC},hxt_wake,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.xthg,d6             ; execute thing
        bra.s   hex_do

;+++
; HOT_WAKE filename \P parameters \J Job name \W Wake name
;---
hxt_wake thg_extn {WAKE},hxt_ldex,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.wake,d6             ; wake thing
        bra.s   hex_do
;+++
; HOT_EXEC filename \P parameters \J Job name \W Wake name
;---
hxt_ldex thg_extn {LDEX},hxt_ldwk,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.xfil,d6             ; execute thing
        bra.s   hex_do

;+++
; HOT_WAKE filename \P parameters \J Job name \W Wake name
;---
hxt_ldwk thg_extn {LDWK},,hxt_prmk

        movem.l hex.reg,-(sp)
        moveq   #hki.wkxf,d6             ; wake thing
hex_do
        move.l  hxt_itnm(a1),a4          ; item name
        sub.l   a2,a2                    ; no new thing

        jsr     hxt_repl                 ; replace
        bne.s   hex_exit

hex_addi
        jsr     hxt_mkxi                 ; make executable item
        bne.s   hex_exit

        jsr     gu_hkuse                 ; use hotkey
        bne.s   hex_rchp

        move.l  a0,a1
        jsr     hk_newst                 ; set new one
        bne.s   hex_rchp

        jsr     gu_hkfre

hex_exit
        tst.l   d0
        movem.l (sp)+,hex.reg
        rts

hex_rchp
        jsr     gu_rchp                  ; remove failed remains
        move.l  a2,d1                    ; any thing added?
        beq.s   hex_exit                 ; ... no
        move.l  a2,a0
        move.l  d0,d3
        moveq   #sms.rthg,d0             ; remove thing
        jsr     gu_thjmp
        move.l  d3,d0
        bra.s   hex_exit

        end
