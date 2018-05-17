; Find Thing utility vector of HOTKEY System II
; Copyright 1989 Jochen Merz

        section gen_util

        xdef    gu_thvec

        include dev8_keys_qdos_sms
        include dev8_keys_thg
        include dev8_keys_err
        include dev8_keys_sys
;+++
; Find Thing utilitiy vector of HOTKEY System II.
; Note this only works if a HOTKEY System version 2.03 or later is present.
;
;               Entry                           Exit
;       d0      vector required                 error code
;       a4                                      Thing Utility Vector
;
;       Error returns:  err.nimp                THING does not exist
;       Condition codes set
;---
gu_thvec
        movem.l d1-d3/a0,-(sp)
        move.w  d0,d3
        moveq   #sms.info,d0            ; get system variables
        trap    #do.sms2
        move.l  sys_thgl(a0),d1         ; this is the Thing list
        beq.s   thvec_nf                ; empty list, very bad!
        move.l  d1,a0
thvec_lp
        move.l  (a0),d1                 ; get next list entry
        beq.s   th_found                ; end of list? Here should be THING!
        move.l  d1,a0                   ; next link
        bra     thvec_lp
thvec_nf
        moveq   #err.nimp,d0            ; THING does not exist
        bra.s   thvec_rt
th_found
        move.l  th_thing(a0),a0         ; get start of Thing
        cmp.l   #-1,thh_type(a0)        ; is it our special THING?
        bne.s   thvec_nf                ; sorry, it isn't
        move.l  (a0,d3.w),a4            ; this is the vector we look for
thvec_rt
        movem.l (sp)+,d1-d3/a0
        tst.l   d0
        rts

        end
