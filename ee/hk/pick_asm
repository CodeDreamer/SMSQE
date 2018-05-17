; HOTKEY Pick / Wake Program   V2.01     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_pick
        xdef    hk_pick1
        xdef    hk_fjob

        xref    hk_cons
        xref    gu_fclos
        xref    hk_nxchn
        xref    hk_cname
        xref    hk_wname

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_qdos_io'
        include 'dev8_ee_hk_data'

;+++
; This routine pick a program whose name is defined by a hotkey item
;
;       a1 c  p pointer to item
;       a6 c  p bottom limit of stack
;
;       status return standard
;---
hk_pick
reglist reg     d1/d2/d3/d4/d5/d6/a0/a1/a2
        movem.l reglist,-(sp)
        moveq   #0,d6                    ; assume pick
        moveq   #$fffffffe,d0
        and.w   hki_type(a1),d0
        cmp.w   #hki.wake,d0             ; or wake?
        beq.s   hkp_wake
        cmp.w   #hki.wkxf,d0
        bne.s   hkp_alljb                ; ... no
hkp_wake
        moveq   #k.wake,d6               ; ... yes
hkp_alljb
        moveq   #0,d4
        moveq   #0,d5
        jsr     hk_wname                 ; expand item name

hkp_jbloop
        move.l  hkd_nxjb(a3),d1          ; next job
        jsr     hk_fjob                  ; find job
        bne.s   hkp_exit
        move.l  d2,hkd_nxjb(a3)          ; new next job

        move.l  d4,d2                    ; number of jobs tried * 4
hkp_jbck
        subq.l  #4,d2
        blt.s   hkp_sjob                 ; none left to check
        cmp.l   (sp,d2.l),d1             ; this job tried?
        bne.s   hkp_jbck                 ; ... no
        beq.s   hkp_exij                 ; ... yes

hkp_sjob
        cmp.l   a6,sp                    ; stack full?
        ble.s   hkp_exij                 ; give up, too many jobs tried
        addq.l  #4,d4                    ; one more tried

        move.l  d1,-(sp)                 ; keep it
        move.l  d6,d2                    ; pick key (0 or k.wake)
        bsr.s   hkp_pchn                 ; pick using arbitrary channel
        bgt.s   hkp_jbloop               ; not this one
        ble.s   hkp_exit                 ; ok or error

hkp_exij
        moveq   #err.ijob,d0

hkp_exit
        add.l   d4,sp                    ; remove stacked IDs
        movem.l (sp)+,reglist
        tst.l   d0
        rts

;+++
; Pick job ID in d1
;
;       d1 c  p
;       d3   s
;       a0   s  channel used for pick
;
;       status returns standard or +1 for unpickable job
;---
hk_pick1
        moveq   #0,d2                    ; no key
hkp_pchn
        sub.l   a0,a0                    ; try channels starting at 0
hkp_chloop
        bsr.s   hkp_pick                 ; pick it
        beq.s   hkp_rts                  ; ... ok
        cmp.l   #err.nc,d0               ; not complete?
        bne.s   hkp_cher                 ; ... no
hkp_chnext
        jsr     hk_nxchn                 ; next channel
        beq.s   hkp_chloop

hkp_open
        jsr     hk_cons                  ; open console owned by d1
        bsr.s   hkp_pick                 ; pick
        jsr     gu_fclos                 ; and close
        beq.s   hkp_rts
hkp_cher
        cmp.l   #err.ijob,d0             ; invalid job
        bne.s   hkp_rts0
        moveq   #1,d0                    ; ... yes, try another
hkp_rts0
        tst.l   d0
hkp_rts
        rts

hkp_pick
        moveq   #iop.pick,d0             ; pick job to top
        movem.l d1/a1,-(sp)
        moveq   #no.wait,d3
        trap    #do.io
        movem.l (sp)+,d1/a1
        tst.l   d0
        rts

;+++
; This routine finds a job with a given (sub)name (a1)
;
;       d1 cr   start job id / job id
;       d2  r   next job id
;       a1 c  p pointer to name
;       status returns standard, if job not found then err.fdnf
;---
hk_fjob
reg.fjob reg    d1/d3/d4/a0/a1
stk_jid equ     $00
stk_name equ    $10
        movem.l reg.fjob,-(sp)
hfj_retry
        move.l  d1,d4                    ; save start
hfj_loop
        move.l  d1,stk_jid(sp)           ; save ID
        moveq   #0,d2                    ; up to owner 0
        moveq   #sms.injb,d0
        trap    #do.sms2
        tst.l   d0
        beq.s   hfj_check                ; ok

        tst.l   stk_jid(sp)              ; was it zero?
        beq.s   hfj_loop                 ; ... yes, no 0

        moveq   #0,d1
        bra.s   hfj_loop                 ; oops, try from zero

hfj_check
        lea     6(a0),a1
        move.l  stk_name(sp),a0
        cmp.w   #$4afb,(a1)+             ; flagged job
        bne.s   hfj_null                 ; no

        tst.w   (a0)                     ; null string?
        beq.s   hfj_next                 ; ... yes, do not check
        jsr     hk_cname                 ; our job?
        beq.s   hfj_exit
        bra.s   hfj_next

hfj_null
        tst.w   (a0)                     ; no name job, null string?
        beq.s   hfj_exit                 ; ... yes

hfj_next
        tst.l   d1                       ; next job
        bne.s   hfj_loop

        moveq   #err.fdnf,d0             ; not found
        tst.l   d4                       ; start at 0?
        bne.s   hfj_retry                ; no, go round again

hfj_exit
        move.l  d1,d2
        movem.l (sp)+,reg.fjob
        tst.l   d0
        rts
        end
