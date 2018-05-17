; Make a list of Thing Users     V0.01   1989  Tony Tebby   QJUMP

        section gut

        xdef    gu_mktul

        xref    gu_mklis
        xref    gu_thjmp

        xref    met_anon
        xref    met_sbas

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_k'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_jbl'

;+++
; Make a list of all Thing users
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1      pointer to Thing name           pointer to list
;       A3      pointer to unique character list
;---
gu_mktul
gtu.reg reg     d2/a0/a2/a3
        movem.l gtu.reg,-(sp)
        moveq   #0,d1                    ; start at beginning of Thing user list
        move.l  a1,a2                    ; thing name
        bsr.l   gti_nuser                ; next user

        moveq   #jbl.elen,d0             ; entry length
        lea     gtu_info,a0              ; how to fill in an entry
        jsr     gu_mklis                 ; make a list
gtu_exit
        movem.l (sp)+,gtu.reg
        rts
;+++
; Fill in next Thing User.  Called from GU_MKLIS at the request of GU_MKTUL
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      next user address               updated
;       A1      entry to fill in                preserved
;       A2      pointer to Thing name           preserved
;       A3      unique character list           updated
;---
gtu_info
gti.reg reg     d2/d3/d4/a0/a1/a4/a5
        movem.l gti.reg,-(sp)
        move.l  a1,a4                   ; keep entry base
        tst.l   d1                      ; all done?
        beq.s   gti_eof                 ; ... yes
        bsr.s   gti_nuser
        bne.s   gti_eof

        move.l  d1,d4                   ; save next user
        move.l  d2,d1
        move.l  d1,jbl_id(a4)           ; fill in this Job ID
        beq.s   gti_sbas

        moveq   #sms.injb,d0            ; get some info on it
        trap    #do.sms2
        tst.l   d0                      ; did it exist?
        bne.s   gti_ok                  ; no, whoops

        move.b  d3,jbl_prty+1(a4)       ; priority

        addq.l  #6,a0
        cmp.w   #$4afb,(a0)+            ; name flag? $$$$
        beq.s   gti_cpnm                ; yes, copy it
        lea     met_anon,a0             ; no, call it '*** Anon ***'
        bra.s   gti_cpnm
gti_sbas
        lea     met_sbas(pc),a0         ; yes, call it that
gti_cpnm
        move.w  (a0)+,d3                ; get actual length
        move.l  a3,d2                   ; unique character required?
        beq.s   gti_ckln                ; ... no
        addq.w  #2,d3                   ; name is two longer
gti_ckln
        moveq   #jbl.maxn,d0            ; name can be this long
        cmp.w   d0,d3                   ; too long?
        ble.s   gti_cpch                ; no
        move.w  d0,d3                   ; yes, use max length
gti_cpch
        lea     jbl_name(a4),a1         ; point to name in entry
        move.w  d3,(a1)+                ; keep length
        tst.l   d2
        beq.s   gti_cpce                ; no additional
        moveq   #k.space,d2
        move.b  (a3)+,(a1)              ; unique character
        cmp.b   (a1)+,d2                ; space?
        bne.s   gti_spce                ; ... no
        subq.l  #1,a3                   ; ... yes, end of character list
gti_spce
        move.b  d2,(a1)+
        subq.w  #2,d3                   ; do not need to copy this much
        bra.s   gti_cpce
gti_cpcl
        move.b  (a0)+,(a1)+             ; copy characters of name
gti_cpce
        dbra    d3,gti_cpcl

gti_ok
        move.l  d4,d1                   ; next user base
        moveq   #0,d0

gti_exit
        movem.l (sp)+,gti.reg
        rts
gti_eof
        moveq   #err.eof,d0
        bra.s   gti_exit

gti_nuser
        moveq   #sms.nthu,d0            ; next thing user
        move.l  a2,a0                   ; thing name
        exg     d1,a1                   ; this usage block
        jsr     gu_thjmp
        exg     a1,d1                   ; next usage block
        move.l  a0,a2                   ; reset next
        rts
        end
