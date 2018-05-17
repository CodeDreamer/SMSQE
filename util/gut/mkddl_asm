; Make a Directory Device list         V0.02   Apr 1988  J.R.Oakley  QJUMP
;                                               Tony Tebby QJUMP
        section gen_util

        xdef    gu_mkddl

        xref    gu_mklis

        include 'dev8_keys_err'
        include 'dev8_keys_iod'
        include 'dev8_keys_sys'
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_dvl'
        include 'dev8_mac_assert'

;+++
; Hack through the directory device list making a list of devices.  This
; is truly nasty and shouldn't be used!  It may be called from user mode,
; as it goes into Supervisor mode for the nasty bits (i.e. most of it).
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1                                      number of entries
;       A1                                      pointer to list
;---
gu_mkddl
mkdreg  reg     d7/a0/a2
        movem.l mkdreg,-(sp)
        move    sr,d7
        trap    #0                       ; in Supervisor mode
        moveq   #sms.info,d0
        trap    #do.sms2                 ; get system info
        lea     sys_fsdl(a0),a2          ; device driver list
        lea     mkd_mknt(pc),a0          ; how to fill in an entry
        moveq   #dvl.elen,d0             ; they're this long
        jsr     gu_mklis(pc)             ; make the list
        move.w  d7,sr                    ; back to previous mode
        tst.l   d0
        movem.l (sp)+,mkdreg
        rts
        page
;+++
; Come here to fill in next device entry
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or ERR.EOF
;       A1      entry to set                    preserved
;       A2      next iod linkage                updated
;---
mkd_mknt
mkdm.reg reg    a0/a1
        movem.l mkdm.reg,-(sp)
mkd_dloop
        move.l  (a2),d0                  ; off end of list yet?
        ble.s   mkd_exef                 ; yes, nothing else to fill in
        move.l  d0,a2                    ; ... next device driver

        assert  dvl_name,0
        lea     iod_dnus-iod_iolk(a2),a0
        move.w  (a0)+,d0                 ; copy name characters
        ble.s   mkd_dloop                ; ... none
        cmp.w   #dvl.mxnm,d0
        ble.s   mkd_cstr
        moveq   #dvl.mxnm,d0
mkd_cstr
        move.w  d0,(a1)+
mkd_cloop
        move.b  (a0)+,(a1)+
        subq.w  #1,d0
        bgt.s   mkd_cloop

        moveq   #0,d0
mkd_exit
        movem.l (sp)+,a0/a1
        rts
mkd_exef
        moveq   #err.eof,d0
        bra.s   mkd_exit
        end
