; Execute code from a Thing (with name)   v0.00   Apr 1988  J.R.Oakley  QJUMP

        section gen_util

        include 'dev8_keys_err'
        include 'dev8_keys_thg'
        include 'dev8_keys_qdos_sms'

        xdef    gu_thexn

        xref    gu_exec
        xref    gu_thjmp

;+++
; This uses the general execute utility gu_EXEC to execute code provided by
; an executable Thing.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      owner, or 0, or -1              job ID
;       D2      priority | timeout              preserved
;       A0      file name                       preserved
;       A1      parameter string                preserved
;       A2      Job name
;       stack   FEXEC $10+gu_EXEC
;---
gu_thexn
gtx.reg  reg     d4/a2/a4
        movem.l gtx.reg,-(sp)
        move.l  a2,a4
        lea     gtx_ucod(pc),a2         ; point to user code
        jsr     gu_exec(pc)             ; and use it to execute a file
        movem.l (sp)+,gtx.reg
        rts
*
gtx_ucod
        bra.s   gtx_read
        nop
        bra.s   gtx_open
        nop
gtx_clos
gxcreg  reg     d0/a0/a1
        movem.l gxcreg,-(sp)
        lea     th_name(a0),a0          ; we're only interested in the name
        moveq   #sms.fthg,d0            ; free the Thing
        jsr     gu_thjmp
        movem.l (sp)+,gxcreg
        tst.l   d0
        rts
;+++
; This copies the Job header, and also gets an instance of the Thing
; for the Job that's going to execute its code.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      job ID                          preserved
;       D2      code length                     preserved
;       D4      extra length (header+name)
;       A0      "channel ID" (Thing)            preserved
;       A1      code space                      preserved
;       A4      program name to be added        preserved
;       A5-A6   unused by this routine          used as required
;
;---
gtx_read
gxr.reg  reg    d3/a0-a4
        movem.l gxr.reg,-(sp)
        lea     (a1,d4.w),a3            ; position of header
        tst.w   d4                      ; any name to add?
        beq.s   gxr_hdr                 ; ... no
        move.w  #$4ef9,(a1)+            ; jmp  .l
        move.l  a3,(a1)+                ; to start
        move.w  #$4afb,(a1)+            ; flag
        move.w  (a4)+,d0                ; name length
        move.w  d0,(a1)+
        bra.s   gxr_nmend
gxr_nmloop
        move.b  (a4)+,(a1)+             ; name
gxr_nmend
        dbra    d0,gxr_nmloop

gxr_hdr
        move.l  th_thing(a0),a4         ; point to thing
        move.l  thh_hdrl(a4),d0         ; size of header
        add.l   thh_hdrs(a4),a4         ; start of header
        bra.s   gxr_hdrend
gxr_hdrloop
        move.b  (a4)+,(a3)+             ; copy header
gxr_hdrend
        subq.l  #1,d0
        bpl.s   gxr_hdrloop

        lea     th_name(a0),a0          ; this Thing
        moveq   #0,d3                   ; now or never
        moveq   #sms.uthg,d0            ; is to be used by the new Job
        jsr     gu_thjmp
gxr_gxrit
        movem.l (sp)+,gxr.reg
        rts
;+++
; Open returns the space required for the job, a "channel ID" for load
; and close, and an absolute start address if required:
; if it fails then the "file" should be "closed".
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1      owner
;       D2                                      code length
;       D3                                      data space length
;       D4                                      extra space for header+name
;       A0      Thing name                      "channel ID" = Thing linkage
;       A1                                      start address or 0
;       A3      start address or zero
;       A4      pointer to program name         preserved
;       A5-A6   unused by this routine          used as required
;
;---
gtx_open
gxo.reg  reg     a2
        movem.l gxo.reg,-(sp)
        moveq   #0,d3
        moveq   #sms.uthg,d0            ; try to use thing
        jsr     gu_thjmp
        bne.s   gxo_exit                ; oops
        move.l  a2,a0                   ; linkage

        moveq   #err.ipar,d0            ; assume the worst
        cmp.l   #thh.flag,thh_flag(a1)  ; standard Thing?
        bne.s   gxo_excl                ; no
        move.l  thh_type(a1),d2         ; get type
        subq.l  #1,d2                   ; is it executable?
        bne.s   gxo_excl                ; ... no

        move.l  thh_hdrl(a1),d2         ; length of file
        move.l  thh_data(a1),d3         ; data space required
        add.l   thh_strt(a1),a1         ; and start address

        move.l  a4,d4                   ; name?
        beq.s   gxo_ok

        move.w  (a4),d0                 ; length of name
        cmp.w   #sms.mxjn,d0            ; enough for expandable job name
        bgt.s   gxo_setn
        moveq   #sms.mxjn,d0
gxo_setn
        moveq   #11,d4
        add.w   d0,d4                   ; length of name header
        bclr    #0,d4
        add.l   d4,d2                   ; total program length
gxo_ok
        moveq   #0,d0
gxo_exit
        movem.l (sp)+,gxo.reg
        rts
gxo_excl
        bsr     gtx_clos
        bra.s   gxo_exit

        end
