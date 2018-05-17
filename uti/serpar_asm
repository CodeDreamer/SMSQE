; Thing routines for ser_par_prt        1989 Jochen Merz V0.00

        section program

        include dev8_keys_thg
        include dev8_keys_qdos_sms

        xdef    ut_serpar

        xref    gu_thjmp

;+++
; Use a routine of the ser_par_prt Thing.
;
;               Entry                   Exit
;       D2.l    routine ID
;       A1      ptr to parameter block  preserved
;
;       Error returns: all usual Thing errors
;---
ut_serpar
        movem.l a0-a3,-(sp)
        bsr.s   spp_use         ; use thing
        bne.s   spp_ret         ; failed?
        jsr     thh_code(a0)    ; jump to code
        bsr.s   spp_free        ; free thing
spp_ret
        movem.l (sp)+,a0-a3
        rts

spp_use
        move.l  a1,-(sp)
        lea     spp_name,a0     ; name
        moveq   #-1,d1          ; for us
        moveq   #120,d3         ; timeout
        moveq   #sms.uthg,d0    ; use thing
        jsr     gu_thjmp
        move.l  a1,a0           ; the address of the thing
        move.l  (sp)+,a1
        rts

spp_free
        move.l  d0,-(sp)        ; keep error code
        lea     spp_name,a0     ; name
        moveq   #-1,d1          ; we used it
        moveq   #sms.fthg,d0
        jsr     gu_thjmp        ; free thing
        move.l  (sp)+,d0
        rts

spp_name
        dc.w    11
        dc.b    'ser_par_prt '

        end
