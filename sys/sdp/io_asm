* Screen Dump IO operations   V2.00       1987  Tony Tebby   QJUMP
*
        section sdp
*
        xdef    sdp_io
*
        xref    sdp_poll
        xref    sdp_psdo
*
        include dev8_keys_qdos_sms
        include dev8_keys_err
        include dev8_keys_sys
        include dev8_keys_qdos_ioa
        include dev8_keys_qdos_io
        include dev8_sys_sdp_ddlk
        include dev8_sys_sdp_data
*
*       IO ops supported
*
*       sbyt    set parm number / set parm
*       smul    ... multiple sbyte
*       defw    define window and dump
*
sdp_io
        cmp.b   #iob.sbyt,d0             ; send byte?
        beq.s   sdp_sbyt                 ; ... yes
        cmp.b   #iob.smul,d0             ; send multiple?
        beq.s   sdp_smul                 ; ... yes
        cmp.b   #iow.defw,d0             ; dump?
        beq.l   sdp_defw                 ; ... yes
*
        moveq   #err.ipar,d0             ; ... not recognised
        rts
*
sdp_snext
        addq.w  #1,d1
        move.b  (a1)+,d4
        bsr.s   sdp_sd4
        bne.s   sdp_rts
sdp_smul
        dbra    d2,sdp_snext
        bra.s   sdp_ok
*
sdp_sbyt
        move.b  d1,d4
sdp_sd4
        move.b  sdd_stat(a3),d5          ; check status
        bgt.s   sdp_pbyt                 ; ... put byte in
        blt.s   sdp_glen                 ; ... get length
*
        cmp.b   #sdd.maxp,d4             ; set parm number
        bhi.s   sdp_orng                 ; ... invalid
        move.b  #1,sdd_stat(a3)          ; read a byte
        move.b  d4,sdd_pptr(a3)          ; and set pointer
        cmp.b   #sdd_dev-sdd_parm,d4     ; is it set name?
        blt.s   sdp_ok                   ; ... no
        bgt.s   sdp_bstrg                ; ... yes, byte string count
        move.b  #-2,sdd_stat(a3)         ; ... yes, set status get length
        bra.s   sdp_ok
sdp_bstrg
        move.b  #-1,sdd_stat(a3)         ; set status get byte length
        bra.s   sdp_ok
*
sdp_glen
        addq.b  #2,sdd_stat(a3)          ; first byte of length?
        beq.s   sdp_szer                 ; ... yes
        cmp.b   #sdd.dlen,d4             ; too long?
        bhi.s   sdp_orng                 ; ... yes
        move.b  d4,sdd_stat(a3)          ; ... no, set length
        addq.b  #1,sdd_stat(a3)          ; (include length in count)
        bra.s   sdp_pbyt
*
sdp_szer
        tst.b   d4                       ; must be zero
        bne.s   sdp_orng                 ; ... oops
*
sdp_pbyt
        moveq   #0,d6
        move.b  sdd_pptr(a3),d6          ; get parm pointer
        move.b  d4,sdd_parm(a3,d6.w)     ; set parm
        subq.b  #1,sdd_stat(a3)          ; one fewer
        addq.b  #1,sdd_pptr(a3)          ; move pointer on
*
        tst.b   sdd_akey(a3)             ; is there an alt key?
        beq.s   sdp_ckunlk               ; ... no
        tst.l   sdd_plad(a3)             ; ... yes, is it set?
        bne.s   sdp_ok                   ; ... yes
        bsr.s   sdp_lpoll                ; ... no, set it
        bra.s   sdp_ok
*
sdp_ckunlk
        tst.l   sdd_plad(a3)             ; is altkey set?
        beq.s   sdp_ok                   ; ... no
        bsr.s   sdp_unlk                 ; ... yes, unlink it
*
sdp_ok
        moveq   #0,d0                    ; ok
sdp_rts
        rts
*
sdp_orng
        moveq   #err.orng,d0
sdp_err
        sf      sdd_stat(a3)             ; next op
        rts
        page
* 
* link in polling interrupt
*
lkreg   reg     d1/d2/a0/a1
sdp_lpoll
        movem.l lkreg,-(sp)
        lea     sdd_dmlk(a3),a4          ; dummy polling link
        lea     sys_poll(a6),a0          ; address of first polling link
sdpl_loop
        move.l  a0,a1                    ; save link
        move.l  (a0),d0
        beq.s   sdpl_link                ; end of list, link in us
        move.l  d0,a0                    ; next address
        swap    d0                       ; in ROM
        tst.w   d0
        bne.s   sdpl_loop
*
        move.l  (a0)+,(a4)+              ; transfer link
        move.l  (a0)+,(a4)+
        subq.l  #8,a4
        move.l  a4,(a1)                  ; and set previous link
        move.l  a1,a0
        bra.s   sdpl_loop                ; carry on

sdpl_link
        lea     sdd_pllk(a3),a4          ; link our polling driver into end
        lea     sdp_poll(pc),a1
        move.l  a1,sdd_plad(a3)
        move.l  a4,(a0)

        movem.l (sp)+,lkreg

        rts

; unlink poll

sdp_unlk
        movem.l lkreg,-(sp)
        lea     sdd_pllk(a3),a0          ; unlink polling address
        moveq   #sms.rpol,d0
        trap    #1
        clr.l   sdd_plad(a3)             ; and clear link
        movem.l (sp)+,lkreg
        rts
        page

sdp_rdef
        cmp.l   sdd_chan(a3),a0          ; our channel dumping?
        bne.s   sdp_ok                   ; ... no
sdp_nc
        moveq   #err.nc,d0               ; not complete
        rts
sdp_iu
        moveq   #err.fdiu,d0             ; in use
        rts
sdp_defw
        tst.w   d3                       ; first entry?
        bne.s   sdp_rdef                 ; ... no
        bset    #0,sdd_go(a3)            ; ready?
        bne.s   sdp_iu                   ; ... no

        jsr     sdp_psdo                 ; set and do
        cmp.l   #err.nc,d0               ; more?
        bne.s   sdpd_clos                ; ... no
        st      sdd_go(a3)               ; ... yes, set go status
        move.l  a0,sdd_chan(a3)          ; no channel dumping
        bra.s   sdpd_exit

sdpd_clos
        move.l  sdd_work+dp_chan(a3),d2  ; any channel opened?
        beq.s   sdp_done                 ; ... no
        move.l  d0,d4                    ; save error
        move.l  d2,a0                    ; set channel
        moveq   #ioa.clos,d0             ; and close
        trap    #2
        clr.l   sdd_work+dp_chan(a3)     ; ... no channel now
        move.l  d4,d0
sdp_done
        clr.l   sdd_chan(a3)             ; no one dumping now
        clr.b   sdd_go(a3)               ; ... not at all
*
sdpd_exit
        rts
        end
