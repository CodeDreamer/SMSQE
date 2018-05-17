* Resident clock programs    1985   T.Tebby   QJUMP
*
        section exten
*
        xdef    tm_susrc
*
        include dev8_sbsext_ext_keys
*
* suspend and read clock (time in d3)
*
tm_susrc
        moveq   #mt.susjb,d0            suspend
        moveq   #myself,d1              myself
        suba.l  a1,a1                   no flag address
        trap    #1
*
        moveq   #mt.rclck,d0            now read the time into d1
        trap    #1
        rts
*
        end
