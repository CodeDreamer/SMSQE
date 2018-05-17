; Make a list of Thing Extensions    V0.01   1989  Tony Tebby   QJUMP

        section gut

        xdef    gu_mktxl

        xref    gu_mklis
        xref    gu_thjmp

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_k'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_txl'

;+++
; Make a list of all Thing Extensions
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1                                      number of entries
;       A1      pointer to Thing name           pointer to list
;       A3      pointer to unique character list
;---
gu_mktxl
gtx.reg reg     d2/d3/d4/a0/a2/a3/a4
        movem.l gtx.reg,-(sp)
        moveq   #0,d4                    ; preset no list
        move.l  a1,a0                    ; Thing name
        move.l  a0,a4                    ; save it
        moveq   #sms.myjb,d1
        moveq   #0,d2                    ; first extension
        moveq   #25,d3                   ; do not wait long
        moveq   #sms.uthg,d0
        jsr     gu_thjmp
        bne.s   gtx_done

        move.l  a1,a2                    ; thing address
        btst    #tht..lst,thh_type(a2)   ; list?
        beq.s   gtx_free

        moveq   #txl.elen,d0             ; entry length
        lea     gtx_info,a0              ; how to fill in an entry
        jsr     gu_mklis                 ; make a list
        move.w  d1,d4                    ; keep length

gtx_free
        moveq   #sms.fthg,d0             ; free thing
        moveq   #sms.myjb,d1
        move.l  a4,a0
        move.l  a1,-(sp)
        jsr     gu_thjmp
        move.l  (sp)+,a1

gtx_done
        move.w  d4,d1                    ; number in list
        bne.s   gtx_exit
        sub.l   a1,a1                    ; ... no list

gtx_exit
        movem.l (sp)+,gtx.reg
        tst.l   d0
        rts
;+++
; Fill in next Thing Ext.  Called from GU_MKLIS at the request of GU_MKTXL
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A1      entry to fill in                preserved
;       A2      pointer to Thing                updated
;       A3      unique character list           updated
;---
gtx_info
        move.l  a2,d0                   ; is there another?
        beq.s   gti_eof
        move.l  a2,txl_base(a1)         ; extension base
        move.l  #$00062020,txl_name(a1) ; string header
        move.l  thh_exid(a2),txl_exid(a1) ; extension ID
        move.l  a3,d0                   ; unique character required?
        beq.s   gti_next                ; ... no
        move.b  (a3)+,txl_name+2(a1)    ; unique character
        cmp.b   #k.space,txl_name+2(a1) ; space?
        bne.s   gti_next                ; ... no
        subq.l  #1,a3                   ; ... yes, end of character list

gti_next
        move.l  thh_next(a2),d0         ; next
        beq.s   gti_snext
        add.l   a2,d0                   ; ... this is it
gti_snext
        move.l  d0,a2
        moveq   #0,d0
gti_exit
        rts
gti_eof
        moveq   #err.eof,d0
        bra.s   gti_exit
        end
