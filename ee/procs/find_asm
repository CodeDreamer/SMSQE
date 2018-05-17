; Find/Use Procedure in the Procedure Thing   V1.00     1990   Tony Tebby   QJUMP

        section procs

        xdef    pr_find
        xdef    pr_usep

        xref    pr_upcpy
        xref    gu_prlis
        xref    gu_thjmp

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_err'
        include 'dev8_mac_assert'
        include 'dev8_ee_procs_data'

;+++
; Find Procedure in the Procedures Thing
;
;       d2  r   extension ID
;       a1 c  p pointer to Thing
;       a2 cr   pointer to procedure name / pointer to Thing name
;---
pr_find
prf.reg reg     a0/a1/a3/a4
        movem.l prf.reg,-(sp)
        bsr.s   prf_look
        movem.l (sp)+,prf.reg
        rts
;+++
; Use Procedure in the Procedures Thing
;
;       d1 cr   Job ID
;       d3 cr   Timeout / version number
;       a1 cr   pointer to procedures thing / pointer to extension thing
;       a2 cr   pointer to procedure name / pointer to thing linkage
;---
pr_usep
pru.reg reg     a0/a3/a4
        movem.l pru.reg,-(sp)
        bsr.s   prf_look
        bne.s   pru_exit

        move.l  a2,a0            ; name of thing to use
        moveq   #sms.uthg,d0
        jsr     gu_thjmp         ; use it

pru_exit
        movem.l (sp)+,pru.reg
        rts

;+++
; Look for procedure name
;---
prf_look
prf.frame equ   prc.mxpn+2

        moveq   #prc.mxpn,d1             ; max length
        cmp.w   (a2),d1                  ; name too long?
        blo.s   prf_nimp
        move.l  a1,a0                    ; save Thing pointer
        sub.w   #prf.frame,sp
        move.l  sp,a1                    ; copy and uppercase name
        jsr     pr_upcpy

        lea     2(sp),a2                 ; our uppercased name characters
        move.l  prc_prcl(a0),a1          ; list of procedures
        lea     prf_proc,a0              ; ... look for it
        jsr     gu_prlis
        add.w   #prf.frame,sp
        beq.s   prf_nimp
        moveq   #0,d0
        rts
prf_nimp
        moveq   #err.nimp,d0
        rts

;+++
; Compare names
;---
prf_proc
        lea     prc_prch(a1),a3
        move.l  a2,a4
        cmp.l   (a3)+,(a4)+
        bne.s   prf_ok
        cmp.l   (a3)+,(a4)+
        bne.s   prf_ok
        cmp.l   (a3)+,(a4)+
        bne.s   prf_ok
        cmp.l   (a3)+,(a4)+
        beq.s   prf_set
prf_ok
        moveq   #0,d0
        rts
prf_set
        move.l  prc_thpt(a1),a2          ; set thing pointer
        move.l  prc_exid(a1),d2          ; and extension
        rts   ; NZ

        end
